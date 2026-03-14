package antony.lappa.inspectrum.service.assessment;

import antony.lappa.inspectrum.controller.dto.assessment.AssessmentCreateRequestDto;
import antony.lappa.inspectrum.exception.AssessmentNotFoundException;
import antony.lappa.inspectrum.mapper.AssessmentMapper;
import antony.lappa.inspectrum.repository.AssessmentRepository;
import antony.lappa.inspectrum.repository.entity.AssessmentEntity;
import antony.lappa.inspectrum.service.model.Assessment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceImplTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private AssessmentMapper assessmentMapper;

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    private UUID userId;
    private UUID assessmentId;
    private AssessmentEntity assessmentEntity;
    private Assessment assessment;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        assessmentId = UUID.randomUUID();

        assessmentEntity = new AssessmentEntity();
        assessmentEntity.setId(assessmentId);
        assessmentEntity.setUserId(userId);
        assessmentEntity.setSubmittedAt(Instant.now());
        assessmentEntity.setCreatedAt(Instant.now());

        assessment = new Assessment();
        assessment.setId(assessmentId);
        assessment.setUserId(userId);
    }

    @Test
    void create_ShouldSaveAndReturn() {
        Map<String, Object> answers = Map.of("q1", "a1");
        AssessmentCreateRequestDto request = new AssessmentCreateRequestDto(answers);

        when(assessmentMapper.toEntity(any(Assessment.class))).thenReturn(assessmentEntity);
        when(assessmentRepository.save(assessmentEntity)).thenReturn(assessmentEntity);
        when(assessmentMapper.toDomain(assessmentEntity)).thenReturn(assessment);

        Assessment result = assessmentService.create(userId, request);

        assertNotNull(result);
        assertEquals(assessmentId, result.getId());
        verify(assessmentRepository).save(assessmentEntity);
    }

    @Test
    void getById_ShouldReturnAssessment_WhenOwned() {
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessmentEntity));
        when(assessmentMapper.toDomain(assessmentEntity)).thenReturn(assessment);

        Assessment result = assessmentService.getById(userId, assessmentId);

        assertNotNull(result);
        assertEquals(assessmentId, result.getId());
    }

    @Test
    void getById_ShouldThrowException_WhenNotOwned() {
        UUID otherUserId = UUID.randomUUID();
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessmentEntity));

        assertThrows(AssessmentNotFoundException.class,
                () -> assessmentService.getById(otherUserId, assessmentId));
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.empty());

        assertThrows(AssessmentNotFoundException.class,
                () -> assessmentService.getById(userId, assessmentId));
    }

    @Test
    void getLatest_ShouldReturnLatest_WhenExists() {
        when(assessmentRepository.findFirstByUserIdOrderBySubmittedAtDesc(userId))
                .thenReturn(Optional.of(assessmentEntity));
        when(assessmentMapper.toDomain(assessmentEntity)).thenReturn(assessment);

        Assessment result = assessmentService.getLatest(userId);

        assertNotNull(result);
        assertEquals(assessmentId, result.getId());
    }

    @Test
    void getLatest_ShouldThrowException_WhenNoneExist() {
        when(assessmentRepository.findFirstByUserIdOrderBySubmittedAtDesc(userId))
                .thenReturn(Optional.empty());

        assertThrows(AssessmentNotFoundException.class,
                () -> assessmentService.getLatest(userId));
    }
}
