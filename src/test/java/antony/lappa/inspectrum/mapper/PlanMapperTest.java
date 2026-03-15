package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.plans.PlanResponseDto;
import antony.lappa.inspectrum.repository.entity.PlanEntity;
import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import antony.lappa.inspectrum.repository.entity.Status;
import antony.lappa.inspectrum.service.model.Plan;
import antony.lappa.inspectrum.service.model.PlanItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlanMapperTest {

    private PlanMapper planMapper;

    @BeforeEach
    void setUp() {
        PlanItemMapper planItemMapper = new PlanItemMapper();
        planMapper = new PlanMapper(planItemMapper);
    }

    @Test
    void toDomain_shouldMapEntityAndItemsToDomain() {
        //given
        PlanEntity entity = new PlanEntity();
        entity.setId(UUID.randomUUID());
        entity.setUserId(UUID.randomUUID());
        entity.setAssessmentId(UUID.randomUUID());
        entity.setTitle("Test Plan");
        entity.setCreatedAt(Instant.now());

        PlanItemEntity itemEntity = new PlanItemEntity();
        itemEntity.setId(UUID.randomUUID());
        itemEntity.setPlanId(entity.getId());
        itemEntity.setResourceId(UUID.randomUUID());
        itemEntity.setStatus(Status.TODO);
        itemEntity.setSortOrder(0);
        itemEntity.setCreatedAt(Instant.now());

        //when
        Plan domain = planMapper.toDomain(entity, List.of(itemEntity));

        //then
        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getUserId(), domain.getUserId());
        assertEquals(entity.getAssessmentId(), domain.getAssessmentId());
        assertEquals(entity.getTitle(), domain.getTitle());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
        assertEquals(1, domain.getItems().size());
        assertEquals(itemEntity.getId(), domain.getItems().get(0).getId());
    }

    @Test
    void toDomain_shouldHandleNullItems() {
        //given
        PlanEntity entity = new PlanEntity();
        entity.setId(UUID.randomUUID());
        entity.setUserId(UUID.randomUUID());
        entity.setAssessmentId(UUID.randomUUID());
        entity.setTitle("Test Plan");
        entity.setCreatedAt(Instant.now());

        //when
        Plan domain = planMapper.toDomain(entity, null);

        //then
        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertNull(domain.getItems());
    }

    @Test
    void toDto_shouldMapDomainToDto() {
        //given
        PlanItem item = new PlanItem();
        item.setId(UUID.randomUUID());
        item.setPlanId(UUID.randomUUID());
        item.setResourceId(UUID.randomUUID());
        item.setStatus(antony.lappa.inspectrum.service.model.Status.TODO);
        item.setSortOrder(0);
        item.setCreatedAt(Instant.now());

        Plan plan = new Plan();
        plan.setId(UUID.randomUUID());
        plan.setUserId(UUID.randomUUID());
        plan.setAssessmentId(UUID.randomUUID());
        plan.setTitle("Test Plan");
        plan.setCreatedAt(Instant.now());
        plan.setItems(List.of(item));

        //when
        PlanResponseDto dto = planMapper.toDto(plan);

        //then
        assertNotNull(dto);
        assertEquals(plan.getId(), dto.getId());
        assertEquals(plan.getTitle(), dto.getTitle());
        assertEquals(plan.getCreatedAt(), dto.getCreatedAt());
        assertEquals(1, dto.getItems().size());
    }

    @Test
    void toDto_shouldReturnNull_whenPlanIsNull() {
        //given
        Plan plan = null;

        //when
        PlanResponseDto dto = planMapper.toDto(plan);

        //then
        assertNull(dto);
    }

    @Test
    void toDto_shouldHandleNullItems() {
        //given
        Plan plan = new Plan();
        plan.setId(UUID.randomUUID());
        plan.setTitle("Test Plan");
        plan.setCreatedAt(Instant.now());
        plan.setItems(null);

        //when
        PlanResponseDto dto = planMapper.toDto(plan);

        //then
        assertNotNull(dto);
        assertNull(dto.getItems());
    }
}
