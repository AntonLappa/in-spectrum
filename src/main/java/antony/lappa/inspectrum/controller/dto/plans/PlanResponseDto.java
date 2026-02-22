package antony.lappa.inspectrum.controller.dto.plans;

import antony.lappa.inspectrum.controller.dto.resource.PlanItemResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponseDto {

    private UUID id;

    private String title;

    private Instant createdAt;

    private List<PlanItemResponseDto> items;
}
