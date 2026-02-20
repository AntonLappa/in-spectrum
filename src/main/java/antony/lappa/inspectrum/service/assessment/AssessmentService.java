package antony.lappa.inspectrum.service.assessment;

import antony.lappa.inspectrum.controller.dto.AssessmentCreateRequestDto;
import antony.lappa.inspectrum.service.model.Assessment;

import java.util.UUID;

public interface AssessmentService {

    Assessment create(UUID userId, AssessmentCreateRequestDto request);

    Assessment getById(UUID userId, UUID assessmentId);

}
