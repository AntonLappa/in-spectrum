package antony.lappa.inspectrum.service.plan;

import antony.lappa.inspectrum.controller.dto.plans.PlanGenerateRequestDto;
import antony.lappa.inspectrum.exception.PlanNotFoundException;
import antony.lappa.inspectrum.mapper.PlanMapper;
import antony.lappa.inspectrum.repository.PlanItemRepository;
import antony.lappa.inspectrum.repository.PlanRepository;
import antony.lappa.inspectrum.repository.entity.PlanEntity;
import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import antony.lappa.inspectrum.service.model.Plan;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanServiceImplTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private PlanItemRepository planItemRepository;

    @Mock
    private PlanMapper planMapper;

    @InjectMocks
    private PlanServiceImpl planService;

    private UUID userId;
    private PlanEntity planEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        planEntity = new PlanEntity();
        planEntity.setId(UUID.randomUUID());
        planEntity.setUserId(userId);
        planEntity.setCreatedAt(Instant.now());
        planEntity.setTitle("Personal plan");
    }

    @Test
    void getCurrent_ShouldReturnPlan_WhenPlanExists() {
        // Arrange
        when(planRepository.findFirstByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(Optional.of(planEntity));
        when(planItemRepository.findAllByPlanIdOrderBySortOrderAsc(planEntity.getId()))
                .thenReturn(Collections.emptyList());

        Plan expectedPlan = new Plan();
        when(planMapper.toDomain(eq(planEntity), anyList()))
                .thenReturn(expectedPlan);

        // Act
        Plan result = planService.getCurrent(userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPlan, result);
        verify(planRepository).findFirstByUserIdOrderByCreatedAtDesc(userId);
        verify(planItemRepository).findAllByPlanIdOrderBySortOrderAsc(planEntity.getId());
    }

    @Test
    void getCurrent_ShouldThrowException_WhenPlanNotFound() {
        // Arrange
        when(planRepository.findFirstByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        PlanNotFoundException exception = assertThrows(PlanNotFoundException.class,
                () -> planService.getCurrent(userId));

        assertTrue(exception.getMessage().contains(userId.toString()));
        verify(planItemRepository, never()).findAllByPlanIdOrderBySortOrderAsc(any());
    }

    @Test
    void generate_ShouldSavePlanAndItems_WhenResourceIdsProvided() {
        // Arrange
        UUID assessmentId = UUID.randomUUID();
        List<UUID> resourceIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        PlanGenerateRequestDto requestDto = new PlanGenerateRequestDto(assessmentId, resourceIds);

        when(planRepository.save(any(PlanEntity.class))).thenAnswer(invocation -> {
            PlanEntity saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        Plan expectedPlan = new Plan();
        when(planMapper.toDomain(any(PlanEntity.class), anyList()))
                .thenReturn(expectedPlan);

        // Act
        Plan result = planService.generate(userId, requestDto);

        // Assert
        assertNotNull(result);
        verify(planRepository).save(any(PlanEntity.class));
        verify(planItemRepository).saveAll(argThat(items -> {
            int count = 0;
            for (Object i : items)
                count++;
            return count == 2;
        }));
    }
}
