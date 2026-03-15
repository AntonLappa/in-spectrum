package antony.lappa.inspectrum.service.assessment;

import antony.lappa.inspectrum.controller.dto.assessment.AssessmentCreateRequestDto;
import antony.lappa.inspectrum.exception.AssessmentNotFoundException;
import antony.lappa.inspectrum.mapper.AssessmentMapper;
import antony.lappa.inspectrum.repository.AssessmentRepository;
import antony.lappa.inspectrum.repository.entity.AssessmentEntity;
import antony.lappa.inspectrum.service.model.Assessment;
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

    @Test
    void create_shouldSaveAndReturnAssessment() {
        //given
        UUID userId = UUID.randomUUID();
        AssessmentCreateRequestDto request = new AssessmentCreateRequestDto(Map.of("q1", "yes"));

        AssessmentEntity savedEntity = new AssessmentEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setUserId(userId);

        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setId(savedEntity.getId());
        expectedAssessment.setUserId(userId);

        when(assessmentMapper.toEntity(any(Assessment.class))).thenReturn(savedEntity);
        when(assessmentRepository.save(savedEntity)).thenReturn(savedEntity);
        when(assessmentMapper.toDomain(savedEntity)).thenReturn(expectedAssessment);

        //when
        Assessment result = assessmentService.create(userId, request);

        //then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(assessmentRepository).save(savedEntity);
        verify(assessmentMapper).toEntity(any(Assessment.class));
        verify(assessmentMapper).toDomain(savedEntity);
    }

    @Test
    void getById_shouldReturnAssessment_whenBelongsToUser() {
        //given
        UUID userId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();

        AssessmentEntity entity = new AssessmentEntity();
        entity.setId(assessmentId);
        entity.setUserId(userId);

        Assessment expected = new Assessment();
        expected.setId(assessmentId);
        expected.setUserId(userId);

        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(entity));
        when(assessmentMapper.toDomain(entity)).thenReturn(expected);

        //when
        Assessment result = assessmentService.getById(userId, assessmentId);

        //then
        assertNotNull(result);
        assertEquals(assessmentId, result.getId());
        assertEquals(userId, result.getUserId());
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        //given
        UUID userId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();

        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(AssessmentNotFoundException.class,
                () -> assessmentService.getById(userId, assessmentId));
    }

    @Test
    void getById_shouldThrowException_whenBelongsToDifferentUser() {
        //given
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();

        AssessmentEntity entity = new AssessmentEntity();
        entity.setId(assessmentId);
        entity.setUserId(otherUserId);

        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(entity));

        //when & then
        assertThrows(AssessmentNotFoundException.class,
                () -> assessmentService.getById(userId, assessmentId));
    }

    @Test
    void getLatest_shouldReturnLatestAssessment() {
        //given
        UUID userId = UUID.randomUUID();

        AssessmentEntity entity = new AssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setUserId(userId);

        Assessment expected = new Assessment();
        expected.setId(entity.getId());

        when(assessmentRepository.findFirstByUserIdOrderBySubmittedAtDesc(userId))
                .thenReturn(Optional.of(entity));
        when(assessmentMapper.toDomain(entity)).thenReturn(expected);

        //when
        Assessment result = assessmentService.getLatest(userId);

        //then
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void getLatest_shouldThrowException_whenNoneFound() {
        //given
        UUID userId = UUID.randomUUID();

        when(assessmentRepository.findFirstByUserIdOrderBySubmittedAtDesc(userId))
                .thenReturn(Optional.empty());

        //when & then
        assertThrows(AssessmentNotFoundException.class,
                () -> assessmentService.getLatest(userId));
    }
}
