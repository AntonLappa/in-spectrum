package antony.lappa.inspectrum.exception;

public class SkillNotFoundException extends RuntimeException {

    public SkillNotFoundException(String code) {
        super("Skill with code " + code + " was not found.");
    }
}
