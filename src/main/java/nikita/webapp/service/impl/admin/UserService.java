package nikita.webapp.service.impl.admin;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.AuthorityName;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.admin.UserHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.repository.nikita.AuthorityRepository;
import nikita.common.repository.nikita.IUserRepository;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.admin.IUserHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.IUserService;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.util.exceptions.UsernameExistsException;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.Constants.SYSTEM;


@Service
public class UserService
        extends NoarkService
        implements IUserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private final IUserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final IUserHateoasHandler userHateoasHandler;
    private final PasswordEncoder encoder;
    private final AdministrativeUnitService administrativeUnitService;

    public UserService(EntityManager entityManager,
                       ApplicationEventPublisher applicationEventPublisher,
                       IPatchService patchService,
                       IUserRepository userRepository,
                       AuthorityRepository authorityRepository,
                       IUserHateoasHandler userHateoasHandler,
                       PasswordEncoder encoder,
                       AdministrativeUnitService administrativeUnitService) {
        super(entityManager, applicationEventPublisher, patchService);
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userHateoasHandler = userHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
        this.encoder = encoder;
        this.administrativeUnitService = administrativeUnitService;
    }

    // All CREATE operations

    @Override
    @Transactional
    public UserHateoas createNewUser(final User user)
            throws UsernameExistsException {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreatedBy(SYSTEM);
        user.setCreatedDate(OffsetDateTime.now());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        UserHateoas userHateoas = new UserHateoas(userRepository.save(user));
        userHateoasHandler.addLinks(userHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, user));

        // If no administrativeUnit is associated with this user, create a
        // default one
        if (user.getAdministrativeUnits().size() < 1) {
            AdministrativeUnit administrativeUnit = new AdministrativeUnit();
            administrativeUnit.setOwnedBy(getUser());
            administrativeUnit.setAdministrativeUnitName("system opprettet " +
                    "AdministrativtEnhet for bruker " + user.getUsername());
            administrativeUnit.setShortName("adminenhet " + user.getUsername());
            administrativeUnit.setDefaultAdministrativeUnit(true);
            administrativeUnit.addUser(user);
            administrativeUnitService.createNewAdministrativeUnitByUser(
                    administrativeUnit, user);
        }
        return userHateoas;
    }

    // All READ operations

    @Override
    public UserHateoas findBySystemID(String systemID) {
        User user = getUserOrThrow(systemID);
        UserHateoas userHateoas = new UserHateoas(user);
        userHateoasHandler.addLinks(userHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, user));
        return userHateoas;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserHateoas findAll() {
        UserHateoas userHateoas = new UserHateoas(
                (List<INoarkEntity>)
                        (List) userRepository.findAll());
        userHateoasHandler.addLinks(userHateoas, new Authorisation());
        return userHateoas;
    }

    /**
     * Check to see is a user with a given email address already exists
     * in the system.
     *
     * @param username The username/emailaddress to check
     * @return true if the username is registered, false otherwise
     */
    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Look up username and return the equivalent User instance if it
     * exist, or null if not.
     *
     * @param username The username/emailaddress to check
     * @return User if the username is registered, null otherwise
     */
    @Override
    public User userGetByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isEmpty() ? null : userOptional.get();
    }

    /**
     * Look up systemID/UUID and return the equivalent User instance
     * if it exist, or null if not.
     *
     * @param systemId UUID for the username to check
     * @return User if the UUID is registered, null otherwise
     */
    @Override
    public User userGetBySystemId(UUID systemId) {
        Optional<User> userOptional = userRepository.findBySystemId(systemId);
        return userOptional.orElse(null);
    }

    /**
     * Check to see is a particular authority already exists
     * in the system.
     *
     * @param authority The authority to check
     * @return true if the authority exists, false otherwise
     */
    @Override
    public boolean authorityExists(AuthorityName authority) {
        return authorityRepository.findByAuthorityName(authority) != null;
    }

    @Override
    @Transactional
    public UserHateoas handleUpdate(@NotNull String userSystemId,
                                    @NotNull Long version,
                                    @NotNull User incomingUser) {

        User existingUser = getUserOrThrow(userSystemId);

        // Copy all the values you are allowed to copy ....
        if (null != incomingUser.getFirstname()) {
            existingUser.setFirstname(incomingUser.getFirstname());
        }
        if (null != incomingUser.getLastname()) {
            existingUser.setLastname(incomingUser.getLastname());
        }

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingUser.setVersion(version);

        UserHateoas userHateoas = new UserHateoas(userRepository.
                save(existingUser));
        userHateoasHandler.addLinks(userHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, existingUser));
        return userHateoas;
    }

    // All DELETE operations

    /**
     * Delete a user identified by the given systemID from the database.
     *
     * @param systemId systemId of the user to delete
     */
    @Override
    @Transactional
    public void deleteEntity(@NotNull String systemId) {
        deleteEntity(getUserOrThrow(systemId));
    }

    /**
     * Delete all user objects
     *
     * @return the number of objects deleted
     */
    @Override
    @Transactional
    public long deleteAll() {
        return userRepository.deleteByUsername(getUser());
    }

    /**
     * Delete all objects belonging to the user identified by username
     *
     * @return the number of objects deleted
     */
    @Override
    @Transactional
    public long deleteByUsername(String username) {
        return userRepository.deleteByUsername(username);
    }

    // All helper methods

    @Override
    public User validateUserReference
            (String type, User user, String username, UUID systemID) {
        if (null == user && null != systemID) {
            user = userGetBySystemId(systemID);
        }
        if (null != user &&
                (!user.getUsername().equals(username)
                        || !user.getSystemId().equals(systemID))) {
            String info = "Inconsistent " + type + " values rejected. ";
            throw new NikitaMalformedInputDataException(info);
        }
        // The values are consistent, return existing user
        return user;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid User back. If there is no valid
     * User, a NoarkEntityNotFoundException exception is thrown
     *
     * @param systemId The systemId of the user object to retrieve
     * @return the user object
     */
    private User getUserOrThrow(@NotNull String systemId) {
        Optional<User> userOptional =
                userRepository.findBySystemId(UUID.fromString(systemId));
        if (userOptional.isEmpty()) {
            String error = INFO_CANNOT_FIND_OBJECT + " User, using systemId " +
                    systemId;
            logger.error(error);
            throw new NoarkEntityNotFoundException(error);
        }
        return userOptional.get();
    }
}
