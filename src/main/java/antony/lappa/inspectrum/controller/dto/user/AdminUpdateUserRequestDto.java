package antony.lappa.inspectrum.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import antony.lappa.inspectrum.controller.dto.Role;
import antony.lappa.inspectrum.controller.dto.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserRequestDto {

    private String name;
    @JsonProperty("phone_number")
    @JsonAlias("phoneNumber")
    private String phoneNumber;
    private String email;
    @JsonProperty("user_type")
    @JsonAlias("userType")
    private UserType userType;
    private Role role;
    private Boolean active;
}
