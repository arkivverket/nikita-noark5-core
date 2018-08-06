package nikita.webapp.util.exceptions;

import nikita.common.util.exceptions.NikitaException;

public class UsernameExistsException extends NikitaException {
    public UsernameExistsException() {
        super();
    }

    public UsernameExistsException(String message) {
        super(message);
    }
}
