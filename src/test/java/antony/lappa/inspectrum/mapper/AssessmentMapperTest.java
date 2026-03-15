package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.assessment.AssessmentResponseDto;
import antony.lappa.inspectrum.repository.entity.AssessmentEntity;
import antony.lappa.inspectrum.service.model.Assessment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentMapperTest {

    private AssessmentMapper assessmentMapper;

    @BeforeEach
    void setUp() {
        assessmentMapper = new AssessmentMapper(new ObjectMapper());
    }

    @Test
    void toDomain_shouldMapEntityToDomain() {
        //given
        Map<String, Object> answers = Map.of("q1", "yes");
        Map<String, Object> result = Map.of("status", "COMPLETED");
        AssessmentEntity entity = new AssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setUserId(UUID.randomUUID());
        entity.setSubmittedAt(Instant.now());
        entity.setAnswerJson("{\"q1\":\"yes\"}");
        entity.setResultJson("{\"status\":\"COMPLETED\"}");
        entity.setCreatedAt(Instant.now());

        //when
        Assessment domain = assessmentMapper.toDomain(entity);

        //then
        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getUserId(), domain.getUserId());
        assertEquals(entity.getSubmittedAt(), domain.getSubmittedAt());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
        assertEquals("yes", domain.getAnswers().get("q1"));
        assertEquals("COMPLETED", domain.getResult().get("status"));
    }

    @Test
    void toDomain_shouldReturnNull_whenEntityIsNull() {
        //given
        AssessmentEntity entity = null;

        //when
        Assessment domain = assessmentMapper.toDomain(entity);

        //then
        assertNull(domain);
    }

    @Test
    void toEntity_shouldMapDomainToEntity() {
        //given
        Assessment assessment = new Assessment();
        assessment.setId(UUID.randomUUID());
        assessment.setUserId(UUID.randomUUID());
        assessment.setSubmittedAt(Instant.now());
        assessment.setAnswers(Map.of("q1", "yes"));
        assessment.setResult(Map.of("status", "COMPLETED"));
        assessment.setCreatedAt(Instant.now());

        //when
        AssessmentEntity entity = assessmentMapper.toEntity(assessment);

        //then
        assertNotNull(entity);
        assertEquals(assessment.getId(), entity.getId());
        assertEquals(assessment.getUserId(), entity.getUserId());
        assertEquals(assessment.getSubmittedAt(), entity.getSubmittedAt());
        assertEquals(assessment.getCreatedAt(), entity.getCreatedAt());
        assertNotNull(entity.getAnswerJson());
        assertNotNull(entity.getResultJson());
    }

    @Test
    void toEntity_shouldReturnNull_whenDomainIsNull() {
        //given
        Assessment assessment = null;

        //when
        AssessmentEntity entity = assessmentMapper.toEntity(assessment);

        //then
        assertNull(entity);
    }

    @Test
    void toDto_shouldMapDomainToDto() {
        //given
        Assessment assessment = new Assessment();
        assessment.setId(UUID.randomUUID());
        assessment.setUserId(UUID.randomUUID());
        assessment.setSubmittedAt(Instant.now());
        assessment.setAnswers(Map.of("q1", "yes"));
        assessment.setResult(Map.of("status", "COMPLETED"));

        //when
        AssessmentResponseDto dto = assessmentMapper.toDto(assessment);

        //then
        assertNotNull(dto);
        assertEquals(assessment.getId(), dto.getId());
        assertEquals(assessment.getUserId(), dto.getUserId());
        assertEquals(assessment.getSubmittedAt(), dto.getSubmittedAt());
        assertEquals(assessment.getAnswers(), dto.getAnswers());
        assertEquals(assessment.getResult(), dto.getResult());
    }

    @Test
    void toDto_shouldReturnNull_whenDomainIsNull() {
        //given
        Assessment assessment = null;

        //when
        AssessmentResponseDto dto = assessmentMapper.toDto(assessment);

        //then
        assertNull(dto);
    }
}
