package antony.lappa.inspectrum.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Plan {

    private UUID id;
    private UUID userId;
    private UUID assessmentId;
    private String title;
    private Instant createdAt;
    private List<PlanItem> items;
}
