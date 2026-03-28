package antony.lappa.inspectrum.controller.dto.progress;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("plan_item_id")
    @JsonAlias("planItemId")
    private UUID planItemId;

    @JsonProperty("entry_date")
    @JsonAlias("entryDate")
    private LocalDate entryDate;

    private String note;

    @JsonProperty("created_at")
    @JsonAlias("createdAt")
    private Instant createdAt;
}
