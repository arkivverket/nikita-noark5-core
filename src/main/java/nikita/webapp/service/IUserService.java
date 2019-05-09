package nikita.webapp.service;

import nikita.common.model.noark5.v4.admin.AuthorityName;
import nikita.common.model.noark5.v4.admin.User;
import nikita.common.model.noark5.v4.hateoas.admin.UserHateoas;
import nikita.webapp.util.exceptions.UsernameExistsException;

public interface IUserService {

    UserHateoas createNewUser(User user) throws UsernameExistsException;

    UserHateoas findAll();

    UserHateoas findByUsername(String username);

    UserHateoas handleUpdate(String userSystemId, Long version, User
            incomingUser);

    boolean userExists(String username);

    boolean authorityExists(AuthorityName authority);
}
