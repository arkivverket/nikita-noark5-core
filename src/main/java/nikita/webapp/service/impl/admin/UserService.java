package nikita.webapp.service.impl.admin;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.AuthorityName;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.admin.UserHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.repository.nikita.AuthorityRepository;
import nikita.common.repository.nikita.IUserRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.admin.IUserHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.IUserService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.util.exceptions.UsernameExistsException;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static nikita.common.config.Constants.SYSTEM;


@Service
@Transactional
public class UserService
        extends NoarkService
        implements IUserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private IUserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private IUserHateoasHandler userHateoasHandler;
    private PasswordEncoder encoder;
    private AdministrativeUnitService administrativeUnitService;

    public UserService(EntityManager entityManager,
                       ApplicationEventPublisher applicationEventPublisher,
                       IUserRepository userRepository,
                       AuthorityRepository authorityRepository,
                       IUserHateoasHandler userHateoasHandler,
                       PasswordEncoder encoder,
                       AdministrativeUnitService administrativeUnitService) {
        super(entityManager, applicationEventPublisher);
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userHateoasHandler = userHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
        this.encoder = encoder;
        this.administrativeUnitService = administrativeUnitService;
    }

    @Override
    public UserHateoas createNewUser(final User user)
            throws UsernameExistsException {
        if (userExists(user.getUsername())) {
            throw new UsernameExistsException("There is an account with that " +
                    "username: " + user.getUsername());
        }
        // Encrypt the password. Should be bcrypt!
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreatedBy(SYSTEM);
        user.setCreatedDate(ZonedDateTime.now());
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

    @Override
    public UserHateoas findByUsername(String username) {
        User user = getUserOrThrow(username);
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
                (List<INikitaEntity>)
                        (List) userRepository.findAll());
        userHateoasHandler.addLinks(userHateoas, new Authorisation());
        return userHateoas;
    }

    @Override
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

    // Helper methods

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

    // All DELETE operations

    /**
     * Delete a user identified by the given systemID from the database.
     *
     * @param systemId systemId of the user to delete
     */
    @Override
    public void deleteEntity(@NotNull String systemId) {
        userRepository.delete(getUserOrThrow(systemId));
    }

    /**
     * Delete all objects belonging to the user identified by username
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByUsername() {
        return userRepository.deleteByUsername(getUser());
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
        Optional<User> userOptional = userRepository.findById(systemId);
        if (!userOptional.isPresent()) {
            String info = systemId + " User, using systemId " +
                    systemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return userOptional.get();
    }
}
