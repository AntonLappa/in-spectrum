package antony.lappa.inspectrum.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String name;
    private String phoneNumber;
}
