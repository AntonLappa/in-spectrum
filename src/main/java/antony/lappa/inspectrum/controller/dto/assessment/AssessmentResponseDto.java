package antony.lappa.inspectrum.controller.dto.assessment;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResponseDto {

    private UUID id;

    @JsonProperty("user_id")
    @JsonAlias("userId")
    private UUID userId;

    @JsonProperty("submitted_at")
    @JsonAlias("submittedAt")
    private Instant submittedAt;

    private Map<String, Object> answers;

    private Map<String, Object> result;
}
