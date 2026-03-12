package antony.lappa.inspectrum.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Progress {

    private UUID id;
    private UUID planItemId;
    private LocalDate entryDate;
    private String note;
    private Instant createdAt;

}
