package antony.lappa.inspectrum.service.user;

import antony.lappa.inspectrum.controller.dto.user.AdminUpdateUserRequestDto;
import antony.lappa.inspectrum.controller.dto.user.UserUpdateRequestDto;
import antony.lappa.inspectrum.exception.UserNotFoundException;
import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.repository.UserRepository;
import antony.lappa.inspectrum.repository.entity.UserEntity;
import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.model.UserType;
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

    @Test
    void findById_shouldReturnUser_whenFound() {
        //given
        UUID userId = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(userId);

        User expectedUser = new User();
        expectedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(entity));
        when(userMapper.toDomain(entity)).thenReturn(expectedUser);

        //when
        User result = userService.findById(userId);

        //then
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        //given
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(UserNotFoundException.class,
                () -> userService.findById(userId));
    }

    @Test
    void findByEmail_shouldReturnUser_whenFound() {
        //given
        String email = "john@example.com";
        UserEntity entity = new UserEntity();
        entity.setEmail(email);

        User expectedUser = new User();
        expectedUser.setEmail(email);

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(entity));
        when(userMapper.toDomain(entity)).thenReturn(expectedUser);

        //when
        User result = userService.findByEmail(email);

        //then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void findByEmail_shouldThrowException_whenNotFound() {
        //given
        String email = "unknown@example.com";

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        //when & then
        assertThrows(UserNotFoundException.class,
                () -> userService.findByEmail(email));
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        //given
        UserEntity entity1 = new UserEntity();
        entity1.setId(UUID.randomUUID());
        UserEntity entity2 = new UserEntity();
        entity2.setId(UUID.randomUUID());

        User user1 = new User();
        user1.setId(entity1.getId());
        User user2 = new User();
        user2.setId(entity2.getId());

        when(userRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(userMapper.toDomain(entity1)).thenReturn(user1);
        when(userMapper.toDomain(entity2)).thenReturn(user2);

        //when
        List<User> result = userService.findAll();

        //then
        assertEquals(2, result.size());
    }

    @Test
    void update_shouldUpdateNameAndPhone() {
        //given
        UUID userId = UUID.randomUUID();
        UserUpdateRequestDto request = new UserUpdateRequestDto("Updated Name", "+380111111111");

        UserEntity entity = new UserEntity();
        entity.setId(userId);
        entity.setName("Old Name");
        entity.setPhoneNumber("+380000000000");

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(userId);
        savedEntity.setName("Updated Name");
        savedEntity.setPhoneNumber("+380111111111");

        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setName("Updated Name");

        when(userRepository.findById(userId)).thenReturn(Optional.of(entity));
        when(userRepository.save(entity)).thenReturn(savedEntity);
        when(userMapper.toDomain(savedEntity)).thenReturn(expectedUser);

        //when
        User result = userService.update(userId, request);

        //then
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(userRepository).save(entity);
    }

    @Test
    void updateAdmin_shouldUpdateAllProvidedFields() {
        //given
        UUID userId = UUID.randomUUID();
        AdminUpdateUserRequestDto request = new AdminUpdateUserRequestDto();
        request.setName("Admin Updated");
        request.setEmail("new@example.com");
        request.setPhoneNumber("+380222222222");
        request.setUserType(antony.lappa.inspectrum.controller.dto.UserType.EDUCATOR);
        request.setRole(antony.lappa.inspectrum.controller.dto.Role.ADMIN);
        request.setActive(false);

        UserEntity entity = new UserEntity();
        entity.setId(userId);
        entity.setName("Old Name");

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(userId);

        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setName("Admin Updated");

        when(userRepository.findById(userId)).thenReturn(Optional.of(entity));
        when(userRepository.save(entity)).thenReturn(savedEntity);
        when(userMapper.toDomain(savedEntity)).thenReturn(expectedUser);

        //when
        User result = userService.updateAdmin(userId, request);

        //then
        assertNotNull(result);
        assertEquals("Admin Updated", result.getName());
        verify(userRepository).save(entity);
    }

    @Test
    void deleteById_shouldDelete_whenUserExists() {
        //given
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(true);

        //when
        userService.deleteById(userId);

        //then
        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteById_shouldThrowException_whenUserNotFound() {
        //given
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(false);

        //when & then
        assertThrows(UserNotFoundException.class,
                () -> userService.deleteById(userId));
    }
}
