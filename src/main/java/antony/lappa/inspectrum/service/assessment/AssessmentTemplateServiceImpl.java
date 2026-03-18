package antony.lappa.inspectrum.service.assessment;

import antony.lappa.inspectrum.exception.AssessmentTemplateLoadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Service
public class AssessmentTemplateServiceImpl implements AssessmentTemplateService {

    private static final String TEMPLATE_PATH = "questionnaires/sensory-assessment-v1.json";

    private final ObjectMapper objectMapper;
    private Map<String, Object> template;

    public AssessmentTemplateServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void loadTemplate() {
        try (InputStream inputStream = new ClassPathResource(TEMPLATE_PATH).getInputStream()) {
            template = objectMapper.readValue(inputStream, new TypeReference<>() {});
            log.info("Assessment template loaded successfully from {}", TEMPLATE_PATH);
        } catch (IOException e) {
            throw new AssessmentTemplateLoadException(TEMPLATE_PATH, e);
        }
    }

    @Override
    public Map<String, Object> getTemplate() {
        return template;
    }
}
