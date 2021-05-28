package nikita.webapp.service.impl.admin;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.AuthorityName;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.admin.UserHateoas;
import nikita.common.repository.nikita.AuthorityRepository;
import nikita.common.repository.nikita.IUserRepository;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.admin.IUserHateoasHandler;
import nikita.webapp.service.IUserService;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.util.exceptions.UsernameExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
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
                       IODataService odataService,
                       IPatchService patchService,
                       IUserRepository userRepository,
                       AuthorityRepository authorityRepository,
                       IUserHateoasHandler userHateoasHandler,
                       PasswordEncoder encoder,
                       AdministrativeUnitService administrativeUnitService) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userHateoasHandler = userHateoasHandler;
        this.encoder = encoder;
        this.administrativeUnitService = administrativeUnitService;
    }

    // All CREATE operations

    @Override
    @Transactional
    public UserHateoas createNewUser(final User user)
            throws UsernameExistsException {

        UserHateoas userHateoas = packAsHateoas(createAndSaveUser(user));

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

    /**
     * Essentially we are just calling createAndSaveUser but I am leaving
     * this as an own method in case we need to add additional logic.
     * Note: There is an assumption here that the user object already contains
     * a reference to the administrativeUnit
     *
     * @param user The incoming user object
     * @throws UsernameExistsException if the username already exists in the
     *                                 database
     */
    public void createNewUserDuringStartup(final User user)
            throws UsernameExistsException {
        createAndSaveUser(user);
    }

    // All READ operations

    @Override
    public UserHateoas findBySystemID(@NotNull final UUID systemId) {
        return packAsHateoas(getUserOrThrow(systemId));
    }

    @Override
    public UserHateoas findAll() {
        return (UserHateoas) odataService.processODataQueryGet();
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
     * Look up systemId/UUID and return the equivalent User instance
     * if it exist, or null if not.
     *
     * @param systemId UUID for the username to check
     * @return User if the UUID is registered, null otherwise
     */
    @Override
    public User userGetBySystemId(@NotNull final UUID systemId) {
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
    public UserHateoas handleUpdate(@NotNull final UUID systemId,
                                    @NotNull final Long version,
                                    @NotNull User incomingUser) {

        User existingUser = getUserOrThrow(systemId);

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
        return packAsHateoas(existingUser);
    }

    // All DELETE operations

    /**
     * Delete a user identified by the given systemId from the database.
     *
     * @param systemId systemId of the user to delete
     */
    @Override
    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        deleteEntity(getUserOrThrow(systemId));
    }

    /**
     * Delete all user objects
     */
    @Override
    @Transactional
    public long deleteAll() {
        return userRepository.deleteByUsername(getUser());
    }

    @Override
    public UserHateoas getDefaultUser() {
        User user = new User();
        user.setUsername("example@example.com");
        user.setFirstname("Hans");
        user.setLastname("Hansen");
        user.setVersion(-1L, true);
        return packAsHateoas(user);
    }

    /**
     * Delete all objects belonging to the user identified by username
     */
    @Override
    @Transactional
    public long deleteByUsername(String username) {
        return userRepository.deleteByUsername(username);
    }

    // All helper methods

    @Override
    public User validateUserReference
            (String type, User user, String username, UUID systemId) {
        if (null == user && null != systemId) {
            user = userGetBySystemId(systemId);
        }
        if (null != user &&
                (!user.getUsername().equals(username)
                        || !user.getSystemId().equals(systemId))) {
            String info = "Inconsistent " + type + " values rejected. ";
            throw new NikitaMalformedInputDataException(info);
        }
        // The values are consistent, return existing user
        return user;
    }

    private User createAndSaveUser(@NotNull final User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreatedBy(SYSTEM);
        user.setCreatedDate(OffsetDateTime.now());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        return userRepository.save(user);
    }

    public UserHateoas packAsHateoas(@NotNull final User user) {
        UserHateoas userHateoas = new UserHateoas(user);
        applyLinksAndHeader(userHateoas, userHateoasHandler);
        return userHateoas;
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
    private User getUserOrThrow(@NotNull final UUID systemId) {
        Optional<User> userOptional =
                userRepository.findBySystemId(systemId);
        if (userOptional.isEmpty()) {
            String error = INFO_CANNOT_FIND_OBJECT + " User, using systemId " +
                    systemId;
            logger.error(error);
            throw new NoarkEntityNotFoundException(error);
        }
        return userOptional.get();
    }
}
