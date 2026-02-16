package antony.lappa.inspectrum.service.token;

import antony.lappa.inspectrum.service.model.Role;

public interface TokenService {

    String createToken(String id, Role role);

    boolean isValidToken(String token);

    String getId(String token);

    Role getRole(String token);

}