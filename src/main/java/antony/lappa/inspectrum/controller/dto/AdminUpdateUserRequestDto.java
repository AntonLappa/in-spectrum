package antony.lappa.inspectrum.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserRequestDto {

    private String name;
    private String phoneNumber;
    private String email;
    private UserType userType;
    private Role role;
    private Boolean active;
}
