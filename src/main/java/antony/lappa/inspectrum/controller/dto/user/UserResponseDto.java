package antony.lappa.inspectrum.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("phone_number")
    @JsonAlias("phoneNumber")
    private String phoneNumber;
    private String email;
    @JsonProperty("user_type")
    @JsonAlias("userType")
    private UserType userType;
    private Role role;
    private boolean active;
    @JsonProperty("created_at")
    @JsonAlias("createdAt")
    private Instant createdAt;

}
