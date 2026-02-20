package antony.lappa.inspectrum.service.user;

import antony.lappa.inspectrum.controller.dto.AdminUpdateUserRequestDto;
import antony.lappa.inspectrum.controller.dto.UserUpdateRequestDto;
import antony.lappa.inspectrum.exception.UserNotFoundException;
import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.repository.UserRepository;
import antony.lappa.inspectrum.repository.entity.UserEntity;
import antony.lappa.inspectrum.repository.entity.Role;
import antony.lappa.inspectrum.repository.entity.UserType;
import antony.lappa.inspectrum.service.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User findCurrentUser() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Finding current user with id: {}", currentUserId);
        return findById(UUID.fromString(currentUserId));
    }

    @Override
    public User findById(UUID id) {
        log.info("Finding user by id: {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDomain(userEntity);
    }

    @Override
    public User findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        UserEntity userEntity = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return userMapper.toDomain(userEntity);
    }

    @Override
    public List<User> findAll() {
        log.info("Finding all users");
        return userRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User update(UUID id, UserUpdateRequestDto request) {
        log.info("Updating user with id: {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (request.getName() != null) {
            userEntity.setName(request.getName());
        }
        if (request.getPhoneNumber() != null) {
            userEntity.setPhoneNumber(request.getPhoneNumber());
        }

        UserEntity savedUserEntity = userRepository.save(userEntity);
        log.info("User with id {} successfully updated", id);
        return userMapper.toDomain(savedUserEntity);
    }

    @Override
    public User updateAdmin(UUID id, AdminUpdateUserRequestDto request) {
        log.info("Admin updating user with id: {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (request.getName() != null) {
            userEntity.setName(request.getName());
        }
        if (request.getPhoneNumber() != null) {
            userEntity.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getEmail() != null) {
            userEntity.setEmail(request.getEmail());
        }
        if (request.getUserType() != null) {
            userEntity.setUserType(UserType.valueOf(request.getUserType().name()));
        }
        if (request.getRole() != null) {
            userEntity.setRole(Role.valueOf(request.getRole().name()));
        }
        if (request.getActive() != null) {
            userEntity.setActive(request.getActive());
        }

        UserEntity savedUserEntity = userRepository.save(userEntity);
        log.info("User with id {} successfully updated by admin", id);
        return userMapper.toDomain(savedUserEntity);
    }

    @Override
    public void deleteById(UUID id) {
        log.info("Deleting user by id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("User with id {} successfully deleted", id);
    }
}
