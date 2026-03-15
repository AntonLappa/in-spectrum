package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.resource.ResourceCreateRequestDto;
import antony.lappa.inspectrum.controller.dto.resource.ResourceResponseDto;
import antony.lappa.inspectrum.repository.entity.ResourceEntity;
import antony.lappa.inspectrum.service.model.Resource;
import antony.lappa.inspectrum.service.model.ResourceAudience;
import antony.lappa.inspectrum.service.model.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ResourceMapperTest {

    private ResourceMapper resourceMapper;

    @BeforeEach
    void setUp() {
        resourceMapper = new ResourceMapper();
    }

    @Test
    void toDomain_fromEntity_shouldMapAllFields() {
        //given
        ResourceEntity entity = new ResourceEntity();
        entity.setId(UUID.randomUUID());
        entity.setSkillId(UUID.randomUUID());
        entity.setType(antony.lappa.inspectrum.repository.entity.ResourceType.VIDEO);
        entity.setAudience(antony.lappa.inspectrum.repository.entity.ResourceAudience.HOME);
        entity.setTitle("Test Resource");
        entity.setDescription("Description");
        entity.setUrl("https://example.com");
        entity.setPublished(true);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        //when
        Resource domain = resourceMapper.toDomain(entity);

        //then
        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getSkillId(), domain.getSkillId());
        assertEquals(ResourceType.VIDEO, domain.getType());
        assertEquals(ResourceAudience.HOME, domain.getAudience());
        assertEquals(entity.getTitle(), domain.getTitle());
        assertEquals(entity.getDescription(), domain.getDescription());
        assertEquals(entity.getUrl(), domain.getUrl());
        assertTrue(domain.isPublished());
        assertEquals(entity.getCreatedBy(), domain.getCreatedBy());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), domain.getUpdatedAt());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        //given
        Resource resource = new Resource();
        resource.setId(UUID.randomUUID());
        resource.setSkillId(UUID.randomUUID());
        resource.setType(ResourceType.TEXT);
        resource.setAudience(ResourceAudience.CLASS);
        resource.setTitle("Test Resource");
        resource.setDescription("Description");
        resource.setUrl("https://example.com");
        resource.setPublished(false);
        resource.setCreatedBy(UUID.randomUUID());
        resource.setCreatedAt(Instant.now());
        resource.setUpdatedAt(Instant.now());

        //when
        ResourceEntity entity = resourceMapper.toEntity(resource);

        //then
        assertNotNull(entity);
        assertEquals(resource.getId(), entity.getId());
        assertEquals(resource.getSkillId(), entity.getSkillId());
        assertEquals(antony.lappa.inspectrum.repository.entity.ResourceType.TEXT, entity.getType());
        assertEquals(antony.lappa.inspectrum.repository.entity.ResourceAudience.CLASS, entity.getAudience());
        assertEquals(resource.getTitle(), entity.getTitle());
        assertEquals(resource.getDescription(), entity.getDescription());
        assertEquals(resource.getUrl(), entity.getUrl());
        assertFalse(entity.isPublished());
        assertEquals(resource.getCreatedBy(), entity.getCreatedBy());
    }

    @Test
    void toResponseDto_shouldMapAllFields() {
        //given
        Resource resource = new Resource();
        resource.setId(UUID.randomUUID());
        resource.setSkillId(UUID.randomUUID());
        resource.setType(ResourceType.IMAGE);
        resource.setAudience(ResourceAudience.BOTH);
        resource.setTitle("Title");
        resource.setDescription("Desc");
        resource.setUrl("https://example.com");
        resource.setPublished(true);
        resource.setCreatedBy(UUID.randomUUID());
        resource.setCreatedAt(Instant.now());
        resource.setUpdatedAt(Instant.now());

        //when
        ResourceResponseDto dto = resourceMapper.toResponseDto(resource);

        //then
        assertNotNull(dto);
        assertEquals(resource.getId(), dto.getId());
        assertEquals(resource.getSkillId(), dto.getSkillId());
        assertEquals(antony.lappa.inspectrum.controller.dto.ResourceType.IMAGE, dto.getType());
        assertEquals(antony.lappa.inspectrum.controller.dto.ResourceAudience.BOTH, dto.getAudience());
        assertEquals(resource.getTitle(), dto.getTitle());
        assertEquals(resource.getDescription(), dto.getDescription());
        assertEquals(resource.getUrl(), dto.getUrl());
        assertEquals(resource.getCreatedBy(), dto.getCreatedBy());
        assertEquals(resource.getCreatedAt(), dto.getCreatedAt());
        assertEquals(resource.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    void toDomain_fromCreateRequestDto_shouldMapAllFields() {
        //given
        ResourceCreateRequestDto dto = new ResourceCreateRequestDto();
        dto.setSkillId(UUID.randomUUID());
        dto.setTitle("New Resource");
        dto.setDescription("New Description");
        dto.setUrl("https://new.example.com");
        dto.setAudience(antony.lappa.inspectrum.controller.dto.ResourceAudience.HOME);
        dto.setType(antony.lappa.inspectrum.controller.dto.ResourceType.VIDEO);
        dto.setPublished(true);

        //when
        Resource domain = resourceMapper.toDomain(dto);

        //then
        assertNotNull(domain);
        assertEquals(dto.getSkillId(), domain.getSkillId());
        assertEquals(dto.getTitle(), domain.getTitle());
        assertEquals(dto.getDescription(), domain.getDescription());
        assertEquals(dto.getUrl(), domain.getUrl());
        assertEquals(ResourceAudience.HOME, domain.getAudience());
        assertEquals(ResourceType.VIDEO, domain.getType());
        assertTrue(domain.isPublished());
    }
}
