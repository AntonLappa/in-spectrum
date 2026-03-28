package antony.lappa.inspectrum.controller.dto.plan_item;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("sort_order")
    @JsonAlias("sortOrder")
    private Integer sortOrder;

    private ResourceShortDto resource;
}
