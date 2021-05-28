package nikita.webapp.service;

import nikita.common.model.noark5.v5.admin.AuthorityName;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.admin.UserHateoas;
import nikita.webapp.util.exceptions.UsernameExistsException;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IUserService {

    UserHateoas createNewUser(@NotNull final User user)
            throws UsernameExistsException;

    void createNewUserDuringStartup(@NotNull final User user)
            throws UsernameExistsException;

    UserHateoas findAll();

    UserHateoas findBySystemID(@NotNull final UUID systemId);

    UserHateoas handleUpdate(@NotNull final UUID systemId,
                             @NotNull final Long version,
                             @NotNull final User incomingUser);

    boolean userExists(@NotNull final String username);

    User userGetByUsername(@NotNull final String username);

    User userGetBySystemId(@NotNull final UUID systemId);

    boolean authorityExists(@NotNull final AuthorityName authority);

    void deleteEntity(@NotNull final UUID systemId);

    long deleteAll();

    long deleteByUsername(@NotNull final String username);

    User validateUserReference(
            @NotNull final String type,
            @NotNull final User user,
            @NotNull final String username,
            @NotNull final UUID systemId);

    UserHateoas getDefaultUser();
}
