package antony.lappa.inspectrum.service.auth;

import antony.lappa.inspectrum.exception.InvalidCredentialsException;
import antony.lappa.inspectrum.exception.UserAlreadyExistException;
import antony.lappa.inspectrum.exception.UserNotFoundException;
import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.repository.UserRepository;
import antony.lappa.inspectrum.repository.entity.UserEntity;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.model.UserType;
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

    @Test
    void signUp_shouldCreateUser_whenEmailNotTaken() {
        //given
        String name = "John Doe";
        String phone = "+380123456789";
        String email = "john@example.com";
        String password = "password123";
        UserType userType = UserType.PARENT;

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setEmail(email);

        User expectedUser = new User();
        expectedUser.setId(savedEntity.getId());
        expectedUser.setEmail(email);

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);
        when(userMapper.toDomain(savedEntity)).thenReturn(expectedUser);

        //when
        User result = authService.signUp(name, phone, email, password, userType);

        //then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository).save(any(UserEntity.class));
        verify(passwordEncoder).encode(password);
    }

    @Test
    void signUp_shouldThrowException_whenEmailAlreadyExists() {
        //given
        String email = "existing@example.com";
        UserEntity existingUser = new UserEntity();
        existingUser.setEmail(email);

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(existingUser));

        //when & then
        assertThrows(UserAlreadyExistException.class,
                () -> authService.signUp("Name", "Phone", email, "pass", UserType.PARENT));
    }

    @Test
    void login_shouldReturnUser_whenCredentialsValid() {
        //given
        String email = "john@example.com";
        String password = "password123";

        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setEmail(email);
        entity.setPasswordHash("hashedPassword");

        User expectedUser = new User();
        expectedUser.setId(entity.getId());

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(true);
        when(userMapper.toDomain(entity)).thenReturn(expectedUser);

        //when
        User result = authService.login(email, password);

        //then
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        //given
        String email = "unknown@example.com";

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        //when & then
        assertThrows(UserNotFoundException.class,
                () -> authService.login(email, "password"));
    }

    @Test
    void login_shouldThrowException_whenPasswordInvalid() {
        //given
        String email = "john@example.com";
        String password = "wrongPassword";

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setPasswordHash("hashedPassword");

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(false);

        //when & then
        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(email, password));
    }
}
