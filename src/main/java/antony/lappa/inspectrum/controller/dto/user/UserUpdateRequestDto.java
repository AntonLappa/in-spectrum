package antony.lappa.inspectrum.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("phone_number")
    @JsonAlias("phoneNumber")
    private String phoneNumber;
}
