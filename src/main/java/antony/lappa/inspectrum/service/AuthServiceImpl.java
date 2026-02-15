package antony.lappa.inspectrum.service;

import antony.lappa.inspectrum.exception.InvalidCredentialsException;
import antony.lappa.inspectrum.exception.UserAlreadyExistException;
import antony.lappa.inspectrum.exception.UserNotFoundException;
import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.repository.entity.UserEntity;
import antony.lappa.inspectrum.repository.UserRepository;
import antony.lappa.inspectrum.service.model.AuthService;
import antony.lappa.inspectrum.repository.entity.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.repository.entity.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User signUp(String name, String phoneNumber, String email, String password,
            antony.lappa.inspectrum.service.model.UserType userType) {

        log.info("Attempting to sign up user with email {}", email);

        Optional<UserEntity> userExists = userRepository.findByEmail(email);

        if (userExists.isPresent()) {
            log.error("User with {} email already exists", email);
            throw new UserAlreadyExistException(email);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);
        userEntity.setPasswordHash(password);
        userEntity.setPhoneNumber(phoneNumber);
        userEntity.setEmail(email);
        userEntity.setUserType(UserType.valueOf(userType.name()));
        userEntity.setRole(Role.USER);
        userEntity.setActive(true);
        userEntity.setCreatedAt(Instant.now());

        UserEntity savedUserEntity = userRepository.save(userEntity);

        log.info("User with email {} succesfulyy created, id={}", email, savedUserEntity.getId());

        return userMapper.toDomain(savedUserEntity);
    }

    @Override
    public User login(String email, String password) {

        log.info("Attempting login for email {}", email);

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Login failed: user with email {} not found", email);
                    return new UserNotFoundException(email);
                });

        if (!userEntity.getPasswordHash().equals(password)) {
            log.error("Login failed: invalid password for email {}", email);
            throw new InvalidCredentialsException();
        }

        log.info("User with email {} successfully logged in", email);

        return userMapper.toDomain(userEntity);
    }
}
