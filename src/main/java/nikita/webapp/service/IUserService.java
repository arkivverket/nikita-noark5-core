package nikita.webapp.service;

import nikita.common.model.noark5.v5.admin.AuthorityName;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.admin.UserHateoas;
import nikita.webapp.util.exceptions.UsernameExistsException;

import javax.validation.constraints.NotNull;

public interface IUserService {

    UserHateoas createNewUser(User user) throws UsernameExistsException;

    UserHateoas findAll();

    UserHateoas findBySystemID(String username);

    UserHateoas handleUpdate(String userSystemId, Long version, User
            incomingUser);

    boolean userExists(String username);

    boolean authorityExists(AuthorityName authority);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAll();

    long deleteByUsername(String username);
}
