package antony.lappa.inspectrum.service.model;

public interface AuthService {

    User signUp(String name,String phoneNumber, String email, String password, UserType userType);

    User login(String email, String password);
}
