package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.auth.SignUpRequestDto;
import antony.lappa.inspectrum.controller.dto.user.UserResponseDto;
import antony.lappa.inspectrum.repository.entity.UserEntity;
import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void toDomain_fromSignUpRequestDto_shouldMapFields() {
        //given
        SignUpRequestDto dto = new SignUpRequestDto();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setPhoneNumber("+380123456789");
        dto.setUserType(UserType.PARENT);

        //when
        User user = userMapper.toDomain(dto);

        //then
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("+380123456789", user.getPhoneNumber());
        assertEquals(UserType.PARENT, user.getUserType());
    }

    @Test
    void toDomain_fromEntity_shouldMapAllFields() {
        //given
        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Jane Doe");
        entity.setEmail("jane@example.com");
        entity.setPhoneNumber("+380987654321");
        entity.setUserType(antony.lappa.inspectrum.repository.entity.UserType.EDUCATOR);
        entity.setRole(antony.lappa.inspectrum.repository.entity.Role.ADMIN);
        entity.setActive(true);
        entity.setCreatedAt(Instant.now());

        //when
        User user = userMapper.toDomain(entity);

        //then
        assertNotNull(user);
        assertEquals(entity.getId(), user.getId());
        assertEquals(entity.getName(), user.getName());
        assertEquals(entity.getEmail(), user.getEmail());
        assertEquals(entity.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(UserType.EDUCATOR, user.getUserType());
        assertEquals(Role.ADMIN, user.getRole());
        assertTrue(user.isActive());
        assertEquals(entity.getCreatedAt(), user.getCreatedAt());
    }

    @Test
    void toDto_shouldMapAllFields() {
        //given
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhoneNumber("+380123456789");
        user.setUserType(UserType.PARENT);
        user.setRole(Role.USER);
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        //when
        UserResponseDto dto = userMapper.toDto(user);

        //then
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(user.getUserType(), dto.getUserType());
        assertEquals(user.getRole(), dto.getRole());
        assertTrue(dto.isActive());
        assertEquals(user.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        //given
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhoneNumber("+380123456789");
        user.setUserType(UserType.PARENT);
        user.setRole(Role.USER);
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        //when
        UserEntity entity = userMapper.toEntity(user);

        //then
        assertNotNull(entity);
        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getName(), entity.getName());
        assertEquals(user.getEmail(), entity.getEmail());
        assertNull(entity.getPasswordHash());
        assertEquals(user.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(antony.lappa.inspectrum.repository.entity.UserType.PARENT, entity.getUserType());
        assertEquals(antony.lappa.inspectrum.repository.entity.Role.USER, entity.getRole());
        assertTrue(entity.isActive());
        assertEquals(user.getCreatedAt(), entity.getCreatedAt());
    }

    @Test
    void updateEntity_shouldUpdateNonNullFields() {
        //given
        User source = new User();
        source.setName("Updated Name");
        source.setEmail("updated@example.com");
        source.setPhoneNumber("+380111111111");
        source.setUserType(UserType.EDUCATOR);
        source.setRole(Role.ADMIN);
        source.setActive(false);

        UserEntity target = new UserEntity();
        target.setName("Old Name");
        target.setEmail("old@example.com");
        target.setPhoneNumber("+380000000000");
        target.setUserType(antony.lappa.inspectrum.repository.entity.UserType.PARENT);
        target.setRole(antony.lappa.inspectrum.repository.entity.Role.USER);
        target.setActive(true);

        //when
        userMapper.updateEntity(source, target);

        //then
        assertEquals("Updated Name", target.getName());
        assertEquals("updated@example.com", target.getEmail());
        assertEquals("+380111111111", target.getPhoneNumber());
        assertEquals(antony.lappa.inspectrum.repository.entity.UserType.EDUCATOR, target.getUserType());
        assertEquals(antony.lappa.inspectrum.repository.entity.Role.ADMIN, target.getRole());
        assertFalse(target.isActive());
    }

    @Test
    void updateEntity_shouldSkipNullFields() {
        //given
        User source = new User();
        source.setActive(true);

        UserEntity target = new UserEntity();
        target.setName("Original Name");
        target.setEmail("original@example.com");
        target.setPhoneNumber("+380000000000");
        target.setUserType(antony.lappa.inspectrum.repository.entity.UserType.PARENT);
        target.setRole(antony.lappa.inspectrum.repository.entity.Role.USER);
        target.setActive(false);

        //when
        userMapper.updateEntity(source, target);

        //then
        assertEquals("Original Name", target.getName());
        assertEquals("original@example.com", target.getEmail());
        assertEquals("+380000000000", target.getPhoneNumber());
        assertEquals(antony.lappa.inspectrum.repository.entity.UserType.PARENT, target.getUserType());
        assertEquals(antony.lappa.inspectrum.repository.entity.Role.USER, target.getRole());
        assertTrue(target.isActive());
    }
}
