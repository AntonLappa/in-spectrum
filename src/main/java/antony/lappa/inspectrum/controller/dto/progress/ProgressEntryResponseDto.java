package antony.lappa.inspectrum.controller.dto.progress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressEntryResponseDto {

    private UUID id;

    private UUID planItemId;

    private LocalDate entryDate;

    private String note;

    private Instant createdAt;
}
