package antony.lappa.inspectrum.controller.dto.plans;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import antony.lappa.inspectrum.controller.dto.plan_item.PlanItemResponseDto;
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

    @JsonProperty("created_at")
    @JsonAlias("createdAt")
    private Instant createdAt;

    private List<PlanItemResponseDto> items;
}
