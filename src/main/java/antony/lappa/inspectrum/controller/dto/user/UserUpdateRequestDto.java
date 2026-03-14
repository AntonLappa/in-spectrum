package antony.lappa.inspectrum.controller.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserUpdateRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;
}
