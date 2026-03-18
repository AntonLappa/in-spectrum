package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.resource.ResourceCreateRequestDto;
import antony.lappa.inspectrum.controller.dto.resource.ResourceResponseDto;
import antony.lappa.inspectrum.repository.entity.ResourceEntity;
import antony.lappa.inspectrum.service.model.Resource;
import antony.lappa.inspectrum.service.model.ResourceType;
import antony.lappa.inspectrum.service.model.ResourceAudience;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResourceMapper {

    public Resource toDomain(ResourceEntity resourceEntity) {
        Resource resource = new Resource();
        resource.setId(resourceEntity.getId());
        resource.setSkillId(resourceEntity.getSkillId());
        resource.setType(ResourceType.valueOf(resourceEntity.getType().name()));
        resource.setAudience(ResourceAudience.valueOf(resourceEntity.getAudience().name()));
        resource.setTitle(resourceEntity.getTitle());
        resource.setDescription(resourceEntity.getDescription());
        resource.setUrl(resourceEntity.getUrl());
        resource.setContent(resourceEntity.getContent());
        resource.setPublished(resourceEntity.isPublished());
        resource.setCreatedBy(resourceEntity.getCreatedBy());
        resource.setCreatedAt(resourceEntity.getCreatedAt());
        resource.setUpdatedAt(resourceEntity.getUpdatedAt());

        return resource;
    }

    public ResourceEntity toEntity(Resource resource) {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setId(resource.getId());
        resourceEntity.setSkillId(resource.getSkillId());
        resourceEntity.setType(antony.lappa.inspectrum.repository.entity.ResourceType.valueOf(resource.getType().name()));
        resourceEntity.setAudience(antony.lappa.inspectrum.repository.entity.ResourceAudience.valueOf(resource.getAudience().name()));
        resourceEntity.setTitle(resource.getTitle());
        resourceEntity.setDescription(resource.getDescription());
        resourceEntity.setUrl(resource.getUrl());
        resourceEntity.setContent(resource.getContent());
        resourceEntity.setPublished(resource.isPublished());
        resourceEntity.setCreatedBy(resource.getCreatedBy());
        resourceEntity.setCreatedAt(resource.getCreatedAt());
        resourceEntity.setUpdatedAt(resource.getUpdatedAt());

        return resourceEntity;
    }

    public ResourceResponseDto toResponseDto(Resource resource) {
        ResourceResponseDto resourceResponseDto = new ResourceResponseDto();
        resourceResponseDto.setId(resource.getId());
        resourceResponseDto.setSkillId(resource.getSkillId());
        resourceResponseDto.setTitle(resource.getTitle());
        resourceResponseDto.setDescription(resource.getDescription());
        resourceResponseDto.setUrl(resource.getUrl());
        resourceResponseDto.setContent(resource.getContent());
        resourceResponseDto.setType(antony.lappa.inspectrum.controller.dto.ResourceType.valueOf(resource.getType().name()));
        resourceResponseDto.setAudience(antony.lappa.inspectrum.controller.dto.ResourceAudience.valueOf(resource.getAudience().name()));
        resourceResponseDto.setPublished(resource.isPublished());
        resourceResponseDto.setCreatedBy(resource.getCreatedBy());
        resourceResponseDto.setCreatedAt(resource.getCreatedAt());
        resourceResponseDto.setUpdatedAt(resource.getUpdatedAt());

        return resourceResponseDto;
    }

    public Resource toDomain(ResourceCreateRequestDto requestDto) {
        Resource resource = new Resource();
        resource.setSkillId(requestDto.getSkillId());
        resource.setTitle(requestDto.getTitle());
        resource.setDescription(requestDto.getDescription());
        resource.setUrl(requestDto.getUrl());
        resource.setContent(requestDto.getContent());
        resource.setAudience(ResourceAudience.valueOf(requestDto.getAudience().name()));
        resource.setType(ResourceType.valueOf(requestDto.getType().name()));
        resource.setPublished(requestDto.getPublished());

        return resource;
    }
}
