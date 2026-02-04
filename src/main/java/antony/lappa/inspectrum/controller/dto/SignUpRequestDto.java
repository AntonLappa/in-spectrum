package antony.lappa.inspectrum.controller.dto;

import antony.lappa.inspectrum.service.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private UserType userType;

}
