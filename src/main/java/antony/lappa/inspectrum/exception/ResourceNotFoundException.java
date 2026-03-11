package antony.lappa.inspectrum.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    public  ResourceNotFoundException(UUID id) {
        super(String.format("Resource with id" + id + "was not found"));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
