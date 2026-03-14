package antony.lappa.inspectrum.service.auth;

import antony.lappa.inspectrum.exception.InvalidCredentialsException;
import antony.lappa.inspectrum.exception.UserAlreadyExistException;
import antony.lappa.inspectrum.exception.UserNotFoundException;
import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.repository.UserRepository;
import antony.lappa.inspectrum.repository.entity.UserEntity;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private String email;
    private String password;
    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() {
        email = "test@example.com";
        password = "password123";

        userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setEmail(email);
        userEntity.setPasswordHash("encodedPassword");

        user = new User();
        user.setId(userEntity.getId());
        user.setEmail(email);
    }

    @Test
    void signUp_ShouldCreateUser_WhenEmailIsNew() {
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });
        when(userMapper.toDomain(any(UserEntity.class))).thenReturn(user);

        User result = authService.signUp("Test", "+123456", email, password, UserType.PARENT);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository).save(any(UserEntity.class));
        verify(passwordEncoder).encode(password);
    }

    @Test
    void signUp_ShouldThrowUserAlreadyExist_WhenEmailExists() {
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(userEntity));

        assertThrows(UserAlreadyExistException.class,
                () -> authService.signUp("Test", "+123456", email, password, UserType.PARENT));

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsValid() {
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        User result = authService.login(email, password);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void login_ShouldThrowUserNotFound_WhenEmailNotFound() {
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> authService.login(email, password));
    }

    @Test
    void login_ShouldThrowInvalidCredentials_WhenPasswordWrong() {
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(email, password));
    }
}
