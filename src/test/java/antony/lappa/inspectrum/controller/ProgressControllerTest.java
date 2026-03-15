package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.progress.ProgressEntryResponseDto;
import antony.lappa.inspectrum.mapper.ProgressMapper;
import antony.lappa.inspectrum.service.model.Progress;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.progress.ProgressService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProgressController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProgressMapper progressMapper;

    @MockitoBean
    private ProgressService progressService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void createProgressEntry_shouldReturn201() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        UUID planItemId = UUID.randomUUID();
        UUID progressId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Progress progress = new Progress();
        progress.setId(progressId);

        ProgressEntryResponseDto responseDto = new ProgressEntryResponseDto();
        responseDto.setId(progressId);
        responseDto.setPlanItemId(planItemId);
        responseDto.setEntryDate(LocalDate.of(2026, 3, 15));
        responseDto.setNote("Test note");
        responseDto.setCreatedAt(Instant.now());

        when(userService.findCurrentUser()).thenReturn(user);
        when(progressService.createProgressEntry(eq(userId), eq(planItemId), any())).thenReturn(progress);
        when(progressMapper.toDto(progress)).thenReturn(responseDto);

        String requestBody = """
                {
                    "entry_date": "2026-03-15",
                    "note": "Test note"
                }
                """;

        //when & then
        mockMvc.perform(post("/plan-items/{planItemId}/progress", planItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(progressId.toString()))
                .andExpect(jsonPath("$.plan_item_id").value(planItemId.toString()));
    }

    @Test
    void getProgressEntries_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        UUID planItemId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Progress progress1 = new Progress();
        progress1.setId(UUID.randomUUID());
        Progress progress2 = new Progress();
        progress2.setId(UUID.randomUUID());

        ProgressEntryResponseDto dto1 = new ProgressEntryResponseDto();
        dto1.setId(progress1.getId());
        ProgressEntryResponseDto dto2 = new ProgressEntryResponseDto();
        dto2.setId(progress2.getId());

        when(userService.findCurrentUser()).thenReturn(user);
        when(progressService.getProgressEntries(userId, planItemId))
                .thenReturn(List.of(progress1, progress2));
        when(progressMapper.toDto(progress1)).thenReturn(dto1);
        when(progressMapper.toDto(progress2)).thenReturn(dto2);

        //when & then
        mockMvc.perform(get("/plan-items/{planItemId}/progress", planItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
