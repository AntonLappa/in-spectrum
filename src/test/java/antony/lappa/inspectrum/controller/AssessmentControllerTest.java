package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.assessment.AssessmentResponseDto;
import antony.lappa.inspectrum.mapper.AssessmentMapper;
import antony.lappa.inspectrum.service.assessment.AssessmentService;
import antony.lappa.inspectrum.service.model.Assessment;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.token.TokenService;
import antony.lappa.inspectrum.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssessmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssessmentService assessmentService;

    @MockitoBean
    private AssessmentMapper assessmentMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void createAssessment_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Assessment assessment = new Assessment();
        assessment.setId(assessmentId);
        assessment.setUserId(userId);

        AssessmentResponseDto responseDto = new AssessmentResponseDto();
        responseDto.setId(assessmentId);
        responseDto.setUserId(userId);
        responseDto.setSubmittedAt(Instant.now());
        responseDto.setAnswers(Map.of("q1", "yes"));
        responseDto.setResult(Map.of("status", "COMPLETED"));

        when(userService.findCurrentUser()).thenReturn(user);
        when(assessmentService.create(eq(userId), any())).thenReturn(assessment);
        when(assessmentMapper.toDto(assessment)).thenReturn(responseDto);

        //when & then
        mockMvc.perform(post("/assessments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answers\":{\"q1\":\"yes\"}}"))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assessmentId.toString()))
                .andExpect(jsonPath("$.user_id").value(userId.toString()));
    }

    @Test
    void getAssessmentById_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Assessment assessment = new Assessment();
        assessment.setId(assessmentId);

        AssessmentResponseDto responseDto = new AssessmentResponseDto();
        responseDto.setId(assessmentId);
        responseDto.setUserId(userId);

        when(userService.findCurrentUser()).thenReturn(user);
        when(assessmentService.getById(userId, assessmentId)).thenReturn(assessment);
        when(assessmentMapper.toDto(assessment)).thenReturn(responseDto);

        //when & then
        mockMvc.perform(get("/assessments/{id}", assessmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assessmentId.toString()));
    }
}
