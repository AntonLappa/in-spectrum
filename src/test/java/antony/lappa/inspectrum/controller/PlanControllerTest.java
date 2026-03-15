package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.plan_item.PlanItemResponseDto;
import antony.lappa.inspectrum.controller.dto.plans.PlanResponseDto;
import antony.lappa.inspectrum.mapper.PlanItemMapper;
import antony.lappa.inspectrum.mapper.PlanMapper;
import antony.lappa.inspectrum.service.model.Plan;
import antony.lappa.inspectrum.service.model.PlanItem;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.plan.PlanService;
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
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlanController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlanMapper planMapper;

    @MockitoBean
    private PlanItemMapper planItemMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PlanService planService;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void generatePlan_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Plan plan = new Plan();
        plan.setId(planId);

        PlanResponseDto responseDto = new PlanResponseDto();
        responseDto.setId(planId);
        responseDto.setTitle("Personal plan");
        responseDto.setCreatedAt(Instant.now());

        when(userService.findCurrentUser()).thenReturn(user);
        when(planService.generate(eq(userId), any())).thenReturn(plan);
        when(planMapper.toDto(plan)).thenReturn(responseDto);

        String requestBody = String.format("""
                {
                    "assessment_id": "%s",
                    "resource_ids": ["%s"]
                }
                """, assessmentId, resourceId);

        //when & then
        mockMvc.perform(post("/plans/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(planId.toString()))
                .andExpect(jsonPath("$.title").value("Personal plan"));
    }

    @Test
    void getCurrent_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Plan plan = new Plan();
        plan.setId(planId);

        PlanResponseDto responseDto = new PlanResponseDto();
        responseDto.setId(planId);
        responseDto.setTitle("Current Plan");

        when(userService.findCurrentUser()).thenReturn(user);
        when(planService.getCurrent(userId)).thenReturn(plan);
        when(planMapper.toDto(plan)).thenReturn(responseDto);

        //when & then
        mockMvc.perform(get("/plans/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(planId.toString()));
    }

    @Test
    void getAllPlans_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Plan plan1 = new Plan();
        plan1.setId(UUID.randomUUID());
        Plan plan2 = new Plan();
        plan2.setId(UUID.randomUUID());

        PlanResponseDto dto1 = new PlanResponseDto();
        dto1.setId(plan1.getId());
        PlanResponseDto dto2 = new PlanResponseDto();
        dto2.setId(plan2.getId());

        when(userService.findCurrentUser()).thenReturn(user);
        when(planService.getAllPlans(userId)).thenReturn(List.of(plan1, plan2));
        when(planMapper.toDto(plan1)).thenReturn(dto1);
        when(planMapper.toDto(plan2)).thenReturn(dto2);

        //when & then
        mockMvc.perform(get("/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateItemStatus_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        PlanItem updatedItem = new PlanItem();
        updatedItem.setId(itemId);

        PlanItemResponseDto responseDto = new PlanItemResponseDto();
        responseDto.setId(itemId);
        responseDto.setStatus(antony.lappa.inspectrum.controller.dto.Status.DONE);

        when(userService.findCurrentUser()).thenReturn(user);
        when(planService.updateItemStatus(eq(userId), eq(itemId), any())).thenReturn(updatedItem);
        when(planItemMapper.toDtoItem(updatedItem)).thenReturn(responseDto);

        String requestBody = """
                {
                    "status": "DONE"
                }
                """;

        //when & then
        mockMvc.perform(patch("/plans/items/{itemId}/status", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId.toString()))
                .andExpect(jsonPath("$.status").value("DONE"));
    }
}
