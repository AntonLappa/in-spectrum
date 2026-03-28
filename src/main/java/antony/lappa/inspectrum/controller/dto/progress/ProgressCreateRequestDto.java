package antony.lappa.inspectrum.controller.dto.progress;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("entry_date")
    @JsonAlias("entryDate")
    private LocalDate entryDate;

    private String note;
}
