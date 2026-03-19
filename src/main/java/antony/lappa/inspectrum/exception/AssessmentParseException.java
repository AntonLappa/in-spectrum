package antony.lappa.inspectrum.exception;

import java.util.UUID;

public class AssessmentParseException extends RuntimeException {

    public AssessmentParseException(UUID assessmentId, Throwable cause) {
        super("Failed to parse answers from assessment with id " + assessmentId, cause);
    }
}
