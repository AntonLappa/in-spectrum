package antony.lappa.inspectrum.controller.dto.auth;

import antony.lappa.inspectrum.service.model.UserType;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    @JsonProperty("phone_number")
    @JsonAlias("phoneNumber")
    private String phoneNumber;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotNull
    @JsonProperty("user_type")
    @JsonAlias("userType")
    private UserType userType;

}
