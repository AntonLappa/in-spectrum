package antony.lappa.inspectrum.service.user;

import antony.lappa.inspectrum.controller.dto.user.UserUpdateRequestDto;
import antony.lappa.inspectrum.exception.UserNotFoundException;
import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.repository.UserRepository;
import antony.lappa.inspectrum.repository.entity.Role;
import antony.lappa.inspectrum.repository.entity.UserEntity;
import antony.lappa.inspectrum.service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
        userEntity.setPhoneNumber("+123456");
        userEntity.setRole(Role.USER);
        userEntity.setCreatedAt(Instant.now());

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        String email = "test@example.com";
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        User result = userService.findByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void findByEmail_ShouldThrowException_WhenEmailNotFound() {
        when(userRepository.findByEmailIgnoreCase("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findByEmail("unknown@example.com"));
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        UserEntity secondEntity = new UserEntity();
        secondEntity.setId(UUID.randomUUID());
        User secondUser = new User();
        secondUser.setId(secondEntity.getId());

        when(userRepository.findAll()).thenReturn(List.of(userEntity, secondEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);
        when(userMapper.toDomain(secondEntity)).thenReturn(secondUser);

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void update_ShouldUpdateFields_WhenUserExists() {
        UserUpdateRequestDto request = new UserUpdateRequestDto();
        request.setName("Updated Name");
        request.setPhoneNumber("+999999");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        User result = userService.update(userId, request);

        assertNotNull(result);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void update_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.update(userId, new UserUpdateRequestDto()));
    }

    @Test
    void deleteById_ShouldDelete_WhenUserExists() {
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(userId));
        verify(userRepository, never()).deleteById(any());
    }
}
