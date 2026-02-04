package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.SignUpRequestDto;
import antony.lappa.inspectrum.controller.dto.UserResponseDto;
import antony.lappa.inspectrum.service.model.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public User toDomain(SignUpRequestDto dto){

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

}
