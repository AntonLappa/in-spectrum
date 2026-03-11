package antony.lappa.inspectrum.service.resource;

import antony.lappa.inspectrum.exception.ResourceNotFoundException;
import antony.lappa.inspectrum.mapper.ResourceMapper;
import antony.lappa.inspectrum.repository.ResourceRepository;
import antony.lappa.inspectrum.repository.entity.ResourceEntity;
import antony.lappa.inspectrum.service.model.Resource;
import antony.lappa.inspectrum.service.model.ResourceAudience;
import antony.lappa.inspectrum.service.model.ResourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    @Override
    public List<Resource> listResource(ResourceType type,
            ResourceAudience audience,
            UUID skillId,
            Boolean isPublished,
            int limit,
            int offset) {
        int page = offset / limit;
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        antony.lappa.inspectrum.repository.entity.ResourceType entityType = null;
        if (type != null) {
            entityType = antony.lappa.inspectrum.repository.entity.ResourceType.valueOf(type.name());
        }

        antony.lappa.inspectrum.repository.entity.ResourceAudience entityAudience = null;
        if (audience != null) {
            entityAudience = antony.lappa.inspectrum.repository.entity.ResourceAudience.valueOf(audience.name());
        }

        Page<ResourceEntity> pageResult = resourceRepository.findResources(
                entityType,
                entityAudience,
                skillId,
                isPublished,
                pageRequest);

        return pageResult.getContent().stream()
                .map(resourceMapper::toDomain)
                .toList();
    }

    @Override
    public Resource getResourceById(UUID resourceId) {
        ResourceEntity resourceEntity = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException(resourceId));
        return resourceMapper.toDomain(resourceEntity);
    }
}
