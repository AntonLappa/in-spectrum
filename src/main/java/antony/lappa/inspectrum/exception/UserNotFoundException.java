package antony.lappa.inspectrum.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String email) {
        super("User with email " + email + " was not found.");
    }
}
