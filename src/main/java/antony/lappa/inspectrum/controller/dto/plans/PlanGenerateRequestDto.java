package antony.lappa.inspectrum.controller.dto.plans;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanGenerateRequestDto {

    @NotNull
    @JsonProperty("assessment_id")
    @JsonAlias("assessmentId")
    private UUID assessmentId;
}

