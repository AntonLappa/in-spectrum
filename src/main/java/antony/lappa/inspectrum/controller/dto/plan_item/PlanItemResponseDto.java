package antony.lappa.inspectrum.controller.dto.plan_item;


import antony.lappa.inspectrum.controller.dto.Status;
import antony.lappa.inspectrum.controller.dto.resource.ResourceShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanItemResponseDto {

    private UUID id;

    private Status status;

    private Integer sortOrder;

    private ResourceShortDto resource;
}
