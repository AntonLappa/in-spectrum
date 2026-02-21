package antony.lappa.inspectrum.service.assessment;

import antony.lappa.inspectrum.controller.dto.AssessmentCreateRequestDto;
import antony.lappa.inspectrum.exception.AssessmentNotFoundException;
import antony.lappa.inspectrum.mapper.AssessmentMapper;
import antony.lappa.inspectrum.repository.AssessmentRepository;
import antony.lappa.inspectrum.repository.entity.AssessmentEntity;
import antony.lappa.inspectrum.service.model.Assessment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentMapper assessmentMapper;

    @Autowired
    public AssessmentServiceImpl(AssessmentRepository assessmentRepository, AssessmentMapper assessmentMapper) {
        this.assessmentRepository = assessmentRepository;
        this.assessmentMapper = assessmentMapper;
    }

    @Override
    public Assessment create(UUID userId, AssessmentCreateRequestDto request) {
        log.info("Creating assessment for user: {}", userId);

        Assessment assessment = new Assessment();
        assessment.setUserId(userId);
        assessment.setSubmittedAt(Instant.now());
        assessment.setAnswers(request.getAnswers());

        // Placeholder for assessment result calculation logic
        Map<String, Object> result = new HashMap<>();
        result.put("status", "COMPLETED");
        assessment.setResult(result);

        AssessmentEntity entity = assessmentMapper.toEntity(assessment);
        AssessmentEntity savedEntity = assessmentRepository.save(entity);

        log.info("Assessment successfully created with id: {}", savedEntity.getId());
        return assessmentMapper.toDomain(savedEntity);
    }

    @Override
    public Assessment getById(UUID userId, UUID assessmentId) {
        log.info("Getting assessment by id: {} for user: {}", assessmentId, userId);

        AssessmentEntity entity = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException(assessmentId));

        if (!entity.getUserId().equals(userId)) {
            log.error("Assessment {} does not belong to user {}", assessmentId, userId);
            throw new AssessmentNotFoundException(assessmentId); // Or AccessDenied if specialized
        }

        return assessmentMapper.toDomain(entity);
    }
}
