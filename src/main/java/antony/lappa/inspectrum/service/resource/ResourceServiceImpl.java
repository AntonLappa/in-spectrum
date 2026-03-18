package antony.lappa.inspectrum.service.resource;

import antony.lappa.inspectrum.exception.AccessDeniedException;
import antony.lappa.inspectrum.exception.InvalidResourceContentException;
import antony.lappa.inspectrum.exception.ResourceNotFoundException;
import antony.lappa.inspectrum.mapper.ResourceMapper;
import antony.lappa.inspectrum.repository.ResourceRepository;
import antony.lappa.inspectrum.repository.entity.ResourceEntity;
import antony.lappa.inspectrum.service.model.Resource;
import antony.lappa.inspectrum.service.model.ResourceAudience;
import antony.lappa.inspectrum.service.model.ResourceType;
import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.user.UserService;
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
    private final UserService userService;

    @Override
    public List<Resource> listResource(ResourceType type,
            ResourceAudience audience,
            UUID skillId,
            Boolean isPublished,
            int limit,
            int offset) {

        User currentUser = userService.findCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            isPublished = true;
        }

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
        Resource resource = resourceMapper.toDomain(resourceEntity);

        User currentUser = userService.findCurrentUser();
        if (!resource.isPublished() && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access Denied");
        }

        return resource;
    }

    @Override
    public Resource createResource(Resource resource) {
        validateResourceContent(resource);

        User currentUser = userService.findCurrentUser();
        resource.setCreatedBy(currentUser.getId());

        ResourceEntity entity = resourceMapper.toEntity(resource);
        ResourceEntity savedEntity = resourceRepository.save(entity);

        log.info("Resource created with id: {}", savedEntity.getId());
        return resourceMapper.toDomain(savedEntity);
    }

    private void validateResourceContent(Resource resource) {
        if (resource.getType() == ResourceType.TEXT) {
            if (resource.getContent() == null || resource.getContent().isBlank()) {
                throw new InvalidResourceContentException("Content is required for TEXT resources");
            }
            if (resource.getUrl() != null && !resource.getUrl().isBlank()) {
                throw new InvalidResourceContentException("URL must be empty for TEXT resources");
            }
        } else if (resource.getType() == ResourceType.VIDEO) {
            if (resource.getUrl() == null || resource.getUrl().isBlank()) {
                throw new InvalidResourceContentException("URL is required for VIDEO resources");
            }
            if (resource.getContent() != null && !resource.getContent().isBlank()) {
                throw new InvalidResourceContentException("Content must be empty for VIDEO resources");
            }
        }
    }
}

