package antony.lappa.inspectrum.exception;

import java.util.UUID;

public class AssessmentNotFoundException extends RuntimeException {

    public AssessmentNotFoundException(UUID id) {
        super("Assessment with id " + id + " was not found.");
    }

    public AssessmentNotFoundException(String message) {
        super(message);
    }
}
