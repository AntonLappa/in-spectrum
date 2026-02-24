package antony.lappa.inspectrum.controller.dto.plans;

import antony.lappa.inspectrum.controller.dto.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanItemStatusUpdateRequestDto {

    private Status status;
}
