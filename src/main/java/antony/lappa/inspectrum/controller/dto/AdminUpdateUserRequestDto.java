package antony.lappa.inspectrum.controller.dto;

import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserRequestDto {

    String name;
    String phoneNumber;
    String email;
    UserType userType;
    Role role;
    Boolean active;
}
