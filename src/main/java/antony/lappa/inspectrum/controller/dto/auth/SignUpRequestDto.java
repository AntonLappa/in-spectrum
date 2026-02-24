package antony.lappa.inspectrum.controller.dto.auth;

import antony.lappa.inspectrum.service.model.UserType;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    private String name;
    @JsonAlias({"phoneNumber", "phone_number"})
    private String phoneNumber;
    private String email;
    private String password;
    @JsonAlias({"userType", "user_type"})
    private UserType userType;

}
