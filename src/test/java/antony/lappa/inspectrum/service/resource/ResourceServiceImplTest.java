package antony.lappa.inspectrum.service.resource;

import antony.lappa.inspectrum.exception.AccessDeniedException;
import antony.lappa.inspectrum.exception.ResourceNotFoundException;
import antony.lappa.inspectrum.mapper.ResourceMapper;
import antony.lappa.inspectrum.repository.ResourceRepository;
import antony.lappa.inspectrum.repository.entity.ResourceEntity;
import antony.lappa.inspectrum.service.model.Resource;
import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceMapper resourceMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private UUID resourceId;
    private ResourceEntity resourceEntity;
    private Resource resource;

    @BeforeEach
    void setUp() {
        resourceId = UUID.randomUUID();

        resourceEntity = new ResourceEntity();
        resourceEntity.setId(resourceId);
        resourceEntity.setTitle("Test Resource");
        resourceEntity.setPublished(true);

        resource = new Resource();
        resource.setId(resourceId);
        resource.setTitle("Test Resource");
        resource.setPublished(true);
    }

    @Test
    void getResourceById_ShouldReturn_WhenPublished() {
        User regularUser = new User();
        regularUser.setRole(Role.USER);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resourceEntity));
        when(resourceMapper.toDomain(resourceEntity)).thenReturn(resource);
        when(userService.findCurrentUser()).thenReturn(regularUser);

        Resource result = resourceService.getResourceById(resourceId);

        assertNotNull(result);
        assertEquals(resourceId, result.getId());
    }

    @Test
    void getResourceById_ShouldReturn_WhenUnpublishedAndAdmin() {
        resource.setPublished(false);

        User adminUser = new User();
        adminUser.setRole(Role.ADMIN);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resourceEntity));
        when(resourceMapper.toDomain(resourceEntity)).thenReturn(resource);
        when(userService.findCurrentUser()).thenReturn(adminUser);

        Resource result = resourceService.getResourceById(resourceId);

        assertNotNull(result);
    }

    @Test
    void getResourceById_ShouldThrowAccessDenied_WhenUnpublishedAndNonAdmin() {
        resource.setPublished(false);

        User regularUser = new User();
        regularUser.setRole(Role.USER);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resourceEntity));
        when(resourceMapper.toDomain(resourceEntity)).thenReturn(resource);
        when(userService.findCurrentUser()).thenReturn(regularUser);

        assertThrows(AccessDeniedException.class,
                () -> resourceService.getResourceById(resourceId));
    }

    @Test
    void getResourceById_ShouldThrowNotFound_WhenMissing() {
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> resourceService.getResourceById(resourceId));
    }

    @Test
    void listResource_ShouldForcePublished_WhenNonAdmin() {
        User regularUser = new User();
        regularUser.setRole(Role.USER);

        Page<ResourceEntity> page = new PageImpl<>(List.of(resourceEntity));

        when(userService.findCurrentUser()).thenReturn(regularUser);
        when(resourceRepository.findResources(isNull(), isNull(), isNull(), eq(true), any(PageRequest.class)))
                .thenReturn(page);
        when(resourceMapper.toDomain(resourceEntity)).thenReturn(resource);

        List<Resource> result = resourceService.listResource(null, null, null, null, 20, 0);

        assertEquals(1, result.size());
        verify(resourceRepository).findResources(isNull(), isNull(), isNull(), eq(true), any(PageRequest.class));
    }
}
