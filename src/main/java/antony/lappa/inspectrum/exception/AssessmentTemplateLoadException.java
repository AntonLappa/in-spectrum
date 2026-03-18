package antony.lappa.inspectrum.exception;

public class AssessmentTemplateLoadException extends RuntimeException {

    public AssessmentTemplateLoadException(String path, Throwable cause) {
        super("Failed to load assessment template from " + path, cause);
    }
}
