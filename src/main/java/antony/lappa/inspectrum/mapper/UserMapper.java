package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.auth.SignUpRequestDto;
import antony.lappa.inspectrum.controller.dto.user.UserResponseDto;
import antony.lappa.inspectrum.repository.entity.UserEntity;
import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.model.UserType;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(SignUpRequestDto dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setUserType(dto.getUserType());

        return user;
    }

    public UserResponseDto toDto(User user) {

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUserType(user.getUserType());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }

    public User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setPhoneNumber(entity.getPhoneNumber());
        user.setUserType(UserType.valueOf(entity.getUserType().name()));
        user.setRole(Role.valueOf(entity.getRole().name()));
        user.setActive(entity.isActive());
        user.setCreatedAt(entity.getCreatedAt());
        return user;
    }

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(null); // Should be handled by service
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setUserType(antony.lappa.inspectrum.repository.entity.UserType.valueOf(user.getUserType().name()));
        entity.setRole(antony.lappa.inspectrum.repository.entity.Role.valueOf(user.getRole().name()));
        entity.setActive(user.isActive());
        entity.setCreatedAt(user.getCreatedAt());
        return entity;
    }

    public void updateEntity(User source, UserEntity target) {
        if (source.getName() != null)
            target.setName(source.getName());
        if (source.getEmail() != null)
            target.setEmail(source.getEmail());
        if (source.getPhoneNumber() != null)
            target.setPhoneNumber(source.getPhoneNumber());
        if (source.getUserType() != null) {
            target.setUserType(antony.lappa.inspectrum.repository.entity.UserType.valueOf(source.getUserType().name()));
        }
        if (source.getRole() != null) {
            target.setRole(antony.lappa.inspectrum.repository.entity.Role.valueOf(source.getRole().name()));
        }
        target.setActive(source.isActive());
    }

}
