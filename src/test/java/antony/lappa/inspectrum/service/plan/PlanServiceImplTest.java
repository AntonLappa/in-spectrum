package antony.lappa.inspectrum.service.plan;

import antony.lappa.inspectrum.controller.dto.plan_item.PlanItemStatusUpdateRequestDto;
import antony.lappa.inspectrum.controller.dto.plans.PlanGenerateRequestDto;
import antony.lappa.inspectrum.exception.PlanNotFoundException;
import antony.lappa.inspectrum.mapper.PlanItemMapper;
import antony.lappa.inspectrum.mapper.PlanMapper;
import antony.lappa.inspectrum.repository.PlanItemRepository;
import antony.lappa.inspectrum.repository.PlanRepository;
import antony.lappa.inspectrum.repository.entity.PlanEntity;
import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import antony.lappa.inspectrum.repository.entity.Status;
import antony.lappa.inspectrum.service.model.Plan;
import antony.lappa.inspectrum.service.model.PlanItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanServiceImplTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private PlanItemRepository planItemRepository;

    @Mock
    private PlanMapper planMapper;

    @Mock
    private PlanItemMapper planItemMapper;

    @InjectMocks
    private PlanServiceImpl planService;

    @Test
    void generate_shouldCreatePlanWithItems() {
        //given
        UUID userId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        UUID resourceId1 = UUID.randomUUID();
        UUID resourceId2 = UUID.randomUUID();

        PlanGenerateRequestDto request = new PlanGenerateRequestDto();
        request.setAssessmentId(assessmentId);
        request.setResourceIds(List.of(resourceId1, resourceId2));

        PlanEntity savedPlan = new PlanEntity();
        savedPlan.setId(UUID.randomUUID());
        savedPlan.setUserId(userId);
        savedPlan.setAssessmentId(assessmentId);

        Plan expectedPlan = new Plan();
        expectedPlan.setId(savedPlan.getId());

        when(planRepository.save(any(PlanEntity.class))).thenReturn(savedPlan);
        when(planMapper.toDomain(eq(savedPlan), anyList())).thenReturn(expectedPlan);

        //when
        Plan result = planService.generate(userId, request);

        //then
        assertNotNull(result);
        assertEquals(savedPlan.getId(), result.getId());
        verify(planRepository).save(any(PlanEntity.class));
        verify(planItemRepository).saveAll(anyList());
    }

    @Test
    void generate_shouldCreatePlanWithoutItems_whenResourceIdsEmpty() {
        //given
        UUID userId = UUID.randomUUID();
        PlanGenerateRequestDto request = new PlanGenerateRequestDto();
        request.setAssessmentId(UUID.randomUUID());
        request.setResourceIds(Collections.emptyList());

        PlanEntity savedPlan = new PlanEntity();
        savedPlan.setId(UUID.randomUUID());

        Plan expectedPlan = new Plan();
        expectedPlan.setId(savedPlan.getId());

        when(planRepository.save(any(PlanEntity.class))).thenReturn(savedPlan);
        when(planMapper.toDomain(eq(savedPlan), eq(Collections.emptyList()))).thenReturn(expectedPlan);

        //when
        Plan result = planService.generate(userId, request);

        //then
        assertNotNull(result);
        verify(planItemRepository, never()).saveAll(anyList());
    }

    @Test
    void getCurrent_shouldReturnCurrentPlan() {
        //given
        UUID userId = UUID.randomUUID();
        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(UUID.randomUUID());
        planEntity.setUserId(userId);

        List<PlanItemEntity> items = List.of(new PlanItemEntity());
        Plan expectedPlan = new Plan();
        expectedPlan.setId(planEntity.getId());

        when(planRepository.findFirstByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(Optional.of(planEntity));
        when(planItemRepository.findAllByPlanIdOrderBySortOrderAsc(planEntity.getId()))
                .thenReturn(items);
        when(planMapper.toDomain(planEntity, items)).thenReturn(expectedPlan);

        //when
        Plan result = planService.getCurrent(userId);

        //then
        assertNotNull(result);
        assertEquals(planEntity.getId(), result.getId());
    }

    @Test
    void getCurrent_shouldThrowException_whenNoPlanFound() {
        //given
        UUID userId = UUID.randomUUID();

        when(planRepository.findFirstByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(Optional.empty());

        //when & then
        assertThrows(PlanNotFoundException.class,
                () -> planService.getCurrent(userId));
    }

    @Test
    void updateItemStatus_shouldUpdateStatusAndReturn() {
        //given
        UUID userId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();

        PlanItemStatusUpdateRequestDto request = new PlanItemStatusUpdateRequestDto();
        request.setStatus(antony.lappa.inspectrum.controller.dto.Status.DONE);

        PlanItemEntity itemEntity = new PlanItemEntity();
        itemEntity.setId(itemId);
        itemEntity.setPlanId(planId);
        itemEntity.setStatus(Status.TODO);

        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(planId);
        planEntity.setUserId(userId);

        PlanItemEntity savedItem = new PlanItemEntity();
        savedItem.setId(itemId);
        savedItem.setStatus(Status.DONE);

        PlanItem expectedItem = new PlanItem();
        expectedItem.setId(itemId);

        when(planItemRepository.findById(itemId)).thenReturn(Optional.of(itemEntity));
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));
        when(planItemRepository.save(itemEntity)).thenReturn(savedItem);
        when(planItemMapper.toDomainItem(savedItem)).thenReturn(expectedItem);

        //when
        PlanItem result = planService.updateItemStatus(userId, itemId, request);

        //then
        assertNotNull(result);
        assertEquals(itemId, result.getId());
        verify(planItemRepository).save(itemEntity);
    }

    @Test
    void updateItemStatus_shouldThrowException_whenItemNotFound() {
        //given
        UUID userId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        PlanItemStatusUpdateRequestDto request = new PlanItemStatusUpdateRequestDto();
        request.setStatus(antony.lappa.inspectrum.controller.dto.Status.DONE);

        when(planItemRepository.findById(itemId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> planService.updateItemStatus(userId, itemId, request));
    }

    @Test
    void updateItemStatus_shouldThrowException_whenUserDoesNotOwnPlan() {
        //given
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();

        PlanItemStatusUpdateRequestDto request = new PlanItemStatusUpdateRequestDto();
        request.setStatus(antony.lappa.inspectrum.controller.dto.Status.DONE);

        PlanItemEntity itemEntity = new PlanItemEntity();
        itemEntity.setId(itemId);
        itemEntity.setPlanId(planId);

        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(planId);
        planEntity.setUserId(otherUserId);

        when(planItemRepository.findById(itemId)).thenReturn(Optional.of(itemEntity));
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));

        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> planService.updateItemStatus(userId, itemId, request));
    }

    @Test
    void getAllPlans_shouldReturnAllPlansForUser() {
        //given
        UUID userId = UUID.randomUUID();
        PlanEntity planEntity1 = new PlanEntity();
        planEntity1.setId(UUID.randomUUID());
        PlanEntity planEntity2 = new PlanEntity();
        planEntity2.setId(UUID.randomUUID());

        Plan plan1 = new Plan();
        plan1.setId(planEntity1.getId());
        Plan plan2 = new Plan();
        plan2.setId(planEntity2.getId());

        when(planRepository.findAllByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(List.of(planEntity1, planEntity2));
        when(planMapper.toDomain(planEntity1, Collections.emptyList())).thenReturn(plan1);
        when(planMapper.toDomain(planEntity2, Collections.emptyList())).thenReturn(plan2);

        //when
        List<Plan> result = planService.getAllPlans(userId);

        //then
        assertEquals(2, result.size());
    }
}
