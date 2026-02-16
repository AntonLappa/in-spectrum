package antony.lappa.inspectrum.service.auth;

import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.model.UserType;

public interface AuthService {

    User signUp(String name, String phoneNumber, String email, String password, UserType userType);

    User login(String email, String password);
}
