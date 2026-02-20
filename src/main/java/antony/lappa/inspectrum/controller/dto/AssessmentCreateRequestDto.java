package antony.lappa.inspectrum.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentCreateRequestDto {

    private Map<String, Object> answers;

}
