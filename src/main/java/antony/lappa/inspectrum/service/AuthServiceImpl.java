package antony.lappa.inspectrum.service;


import antony.lappa.inspectrum.exception.UserAlreadyExistException;
import antony.lappa.inspectrum.exception.UserNotFoundException;
import antony.lappa.inspectrum.service.model.AuthService;
import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.model.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private HashMap<UUID, User> users = new HashMap<>();


    @Override
    public User signUp(String name, String phoneNumber, String email, String password, UserType userType) {

        log.info("Attempting to sign up user with email {}", email);

        Optional<User> userExists = users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();

        if (userExists.isPresent()) {
            log.error("User with {} email already exists", email);
            throw new UserAlreadyExistException(email);
        }




        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setUserType(userType);
        user.setRole(Role.USER);
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        users.put(user.getId(), user);

        log.info("User with email {} succesfulyy created, id={}", email, user.getId());

        return user;
    }

    @Override
    public User login(String email, String password) {

        log.info("Attempting login for email {}", email);

        User user = users.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Login failed: user with email {} not found", email);
                    return new UserNotFoundException(email);
                });

        log.info("User with email {} successfully logged in", email);

        return user;
    }
}
