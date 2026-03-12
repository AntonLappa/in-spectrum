package antony.lappa.inspectrum.controller.dto.progress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressCreateRequestDto {

    private LocalDate entryDate;

    private String note;
}
