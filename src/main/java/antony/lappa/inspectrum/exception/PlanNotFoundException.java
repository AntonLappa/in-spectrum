package antony.lappa.inspectrum.exception;

import java.util.UUID;

public class PlanNotFoundException extends RuntimeException {

    public PlanNotFoundException(UUID id) {super("Plan with id " + id + " was not found");}

    public PlanNotFoundException(String message) {
        super(message);
    }
}
