package antony.lappa.inspectrum.exception;

import java.util.UUID;

public class PlanItemNotFoundException extends RuntimeException {

    public PlanItemNotFoundException(UUID id) {
        super("PlanItem with id " + id + " was not found");
    }

    public PlanItemNotFoundException(String message) {
        super(message);
    }
}
