package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.plan_item.PlanItemResponseDto;
import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import antony.lappa.inspectrum.repository.entity.Status;
import antony.lappa.inspectrum.service.model.PlanItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlanItemMapperTest {

    private PlanItemMapper planItemMapper;

    @BeforeEach
    void setUp() {
        planItemMapper = new PlanItemMapper(new ResourceMapper());
    }

    @Test
    void toDomainItem_shouldMapEntityToDomain() {
        //given
        PlanItemEntity entity = new PlanItemEntity();
        entity.setId(UUID.randomUUID());
        entity.setPlanId(UUID.randomUUID());
        entity.setResourceId(UUID.randomUUID());
        entity.setStatus(Status.TODO);
        entity.setSortOrder(1);
        entity.setCreatedAt(Instant.now());

        //when
        PlanItem domain = planItemMapper.toDomainItem(entity);

        //then
        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getPlanId(), domain.getPlanId());
        assertEquals(entity.getResourceId(), domain.getResourceId());
        assertEquals(antony.lappa.inspectrum.service.model.Status.TODO, domain.getStatus());
        assertEquals(entity.getSortOrder(), domain.getSortOrder());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
    }

    @Test
    void toDomainItem_shouldReturnNull_whenEntityIsNull() {
        //given
        PlanItemEntity entity = null;

        //when
        PlanItem domain = planItemMapper.toDomainItem(entity);

        //then
        assertNull(domain);
    }

    @Test
    void toDtoItem_shouldMapDomainToDto() {
        //given
        PlanItem item = new PlanItem();
        item.setId(UUID.randomUUID());
        item.setPlanId(UUID.randomUUID());
        item.setResourceId(UUID.randomUUID());
        item.setStatus(antony.lappa.inspectrum.service.model.Status.DONE);
        item.setSortOrder(2);
        item.setCreatedAt(Instant.now());

        //when
        PlanItemResponseDto dto = planItemMapper.toDtoItem(item);

        //then
        assertNotNull(dto);
        assertEquals(item.getId(), dto.getId());
        assertEquals(antony.lappa.inspectrum.controller.dto.Status.DONE, dto.getStatus());
        assertEquals(item.getSortOrder(), dto.getSortOrder());
        assertNull(dto.getResource());
    }

    @Test
    void toDtoItem_shouldReturnNull_whenDomainIsNull() {
        //given
        PlanItem item = null;

        //when
        PlanItemResponseDto dto = planItemMapper.toDtoItem(item);

        //then
        assertNull(dto);
    }
}
