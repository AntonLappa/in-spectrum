package antony.lappa.inspectrum.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String phoneNumber;
}
