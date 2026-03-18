package antony.lappa.inspectrum.service.assessment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentTemplateServiceTest {

    private final AssessmentTemplateServiceImpl service = new AssessmentTemplateServiceImpl(new ObjectMapper());

    @Test
    void loadTemplate_shouldLoadJsonSuccessfully() {
        //given & when
        service.loadTemplate();

        //then
        Map<String, Object> template = service.getTemplate();
        assertNotNull(template);
        assertEquals("sensory-assessment-v1", template.get("id"));
        assertNotNull(template.get("title"));
        assertInstanceOf(List.class, template.get("questions"));

        List<?> questions = (List<?>) template.get("questions");
        assertEquals(10, questions.size());
    }

    @Test
    void getTemplate_shouldReturnSameInstance() {
        //given
        service.loadTemplate();

        //when
        Map<String, Object> first = service.getTemplate();
        Map<String, Object> second = service.getTemplate();

        //then
        assertSame(first, second);
    }
}
