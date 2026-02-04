package antony.lappa.inspectrum.service;


import antony.lappa.inspectrum.service.model.AuthService;
import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.model.UserType;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private HashMap<UUID, User> users = new HashMap<>();


    @Override
    public User signUp(String name, String phoneNumber, String email, String password, UserType userType) {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setUserType(userType);
        user.setRole(Role.USER);
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        return user;
    }

    @Override
    public User login(String email, String password) {

        return users.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
