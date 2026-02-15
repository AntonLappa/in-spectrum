package antony.lappa.inspectrum.controller.dto;

import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserResponseDto {

    private UUID id;
    private String name;
    private String phoneNumber;
    private String email;
    private UserType userType;
    private Role role;
    private boolean active;
    private Instant createdAt;

}
