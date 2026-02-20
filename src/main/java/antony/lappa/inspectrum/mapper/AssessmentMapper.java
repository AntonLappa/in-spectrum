package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.AssessmentResponseDto;
import antony.lappa.inspectrum.repository.entity.AssessmentEntity;
import antony.lappa.inspectrum.service.model.Assessment;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AssessmentMapper {

    private final ObjectMapper objectMapper;

    public Assessment toDomain(AssessmentEntity entity) {
        if (entity == null) {
            return null;
        }

        Assessment assessment = new Assessment();
        assessment.setId(entity.getId());
        assessment.setUserId(entity.getUserId());
        assessment.setSubmittedAt(entity.getSubmittedAt());
        assessment.setAnswers(parseJson(entity.getAnswerJson()));
        assessment.setResult(parseJson(entity.getResultJson()));
        assessment.setCreatedAt(entity.getCreatedAt());

        return assessment;
    }

    public AssessmentEntity toEntity(Assessment assessment) {
        if (assessment == null) {
            return null;
        }

        AssessmentEntity entity = new AssessmentEntity();
        entity.setId(assessment.getId());
        entity.setUserId(assessment.getUserId());
        entity.setSubmittedAt(assessment.getSubmittedAt());
        entity.setAnswerJson(toJson(assessment.getAnswers()));
        entity.setResultJson(toJson(assessment.getResult()));
        entity.setCreatedAt(assessment.getCreatedAt());

        return entity;
    }

    public AssessmentResponseDto toDto(Assessment assessment) {
        if (assessment == null) {
            return null;
        }

        AssessmentResponseDto dto = new AssessmentResponseDto();
        dto.setId(assessment.getId());
        dto.setUserId(assessment.getUserId());
        dto.setSubmittedAt(assessment.getSubmittedAt());
        dto.setAnswers(assessment.getAnswers());
        dto.setResult(assessment.getResult());

        return dto;
    }

    private String toJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JacksonException e) {
            throw new RuntimeException("Error converting map to JSON", e);
        }
    }

    private Map<String, Object> parseJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JacksonException e) {
            throw new RuntimeException("Error parsing JSON to map", e);
        }
    }
}
