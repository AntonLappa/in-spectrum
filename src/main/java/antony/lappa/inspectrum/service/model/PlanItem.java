package antony.lappa.inspectrum.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlanItem {

    private UUID id;
    private UUID planId;
    private UUID resourceId;
    private Status status;
    private Integer sortOrder;
    private Instant createdAt;
}
