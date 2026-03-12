package antony.lappa.inspectrum.exception;

import java.util.UUID;

public class ProgressNotFoundException extends RuntimeException {

    public ProgressNotFoundException(UUID userId) { super(String.format("Progress with id " + userId + " was not found")); }

    public ProgressNotFoundException(String message) {
        super(message);
    }
}
