package antony.lappa.inspectrum.controller.dto.plans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanGenerateRequestDto {

    private UUID assessmentId;

    private List<UUID> resourceIds;
}
