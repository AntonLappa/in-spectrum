package antony.lappa.inspectrum.controller.dto.progress;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressCreateRequestDto {

    @NotNull
    private LocalDate entryDate;

    private String note;
}
