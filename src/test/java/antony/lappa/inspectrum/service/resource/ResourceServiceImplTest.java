package antony.lappa.inspectrum.service.resource;

import antony.lappa.inspectrum.exception.AccessDeniedException;
import antony.lappa.inspectrum.exception.ResourceNotFoundException;
import antony.lappa.inspectrum.mapper.ResourceMapper;
import antony.lappa.inspectrum.repository.ResourceRepository;
import antony.lappa.inspectrum.repository.entity.ResourceEntity;
import antony.lappa.inspectrum.service.model.Resource;
import antony.lappa.inspectrum.service.model.ResourceType;
import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Test
    void listResource_shouldForcePublishedTrue_forNonAdmin() {
        //given
        User nonAdminUser = new User();
        nonAdminUser.setRole(Role.USER);

        ResourceEntity entity = new ResourceEntity();
        Resource resource = new Resource();

        Page<ResourceEntity> page = new PageImpl<>(List.of(entity));

        when(userService.findCurrentUser()).thenReturn(nonAdminUser);
        when(resourceRepository.findResources(any(), any(), any(), eq(true), any(Pageable.class)))
                .thenReturn(page);
        when(resourceMapper.toDomain(entity)).thenReturn(resource);

        //when
        List<Resource> result = resourceService.listResource(null, null, null, null, 20, 0);

        //then
        assertEquals(1, result.size());
        verify(resourceRepository).findResources(any(), any(), any(), eq(true), any(Pageable.class));
    }

    @Test
    void listResource_shouldAllowUnpublished_forAdmin() {
        //given
        User adminUser = new User();
        adminUser.setRole(Role.ADMIN);

        Page<ResourceEntity> page = new PageImpl<>(List.of());

        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(resourceRepository.findResources(any(), any(), any(), eq(false), any(Pageable.class)))
                .thenReturn(page);

        //when
        List<Resource> result = resourceService.listResource(null, null, null, false, 20, 0);

        //then
        assertNotNull(result);
        verify(resourceRepository).findResources(any(), any(), any(), eq(false), any(Pageable.class));
    }

    @Test
    void getResourceById_shouldReturnResource_whenPublished() {
        //given
        UUID resourceId = UUID.randomUUID();

        ResourceEntity entity = new ResourceEntity();
        entity.setId(resourceId);
        entity.setPublished(true);

        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setPublished(true);

        User user = new User();
        user.setRole(Role.USER);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(entity));
        when(resourceMapper.toDomain(entity)).thenReturn(resource);
        when(userService.findCurrentUser()).thenReturn(user);

        //when
        Resource result = resourceService.getResourceById(resourceId);

        //then
        assertNotNull(result);
        assertEquals(resourceId, result.getId());
    }

    @Test
    void getResourceById_shouldReturnResource_whenUnpublishedAndAdmin() {
        //given
        UUID resourceId = UUID.randomUUID();

        ResourceEntity entity = new ResourceEntity();
        entity.setId(resourceId);
        entity.setPublished(false);

        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setPublished(false);

        User admin = new User();
        admin.setRole(Role.ADMIN);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(entity));
        when(resourceMapper.toDomain(entity)).thenReturn(resource);
        when(userService.findCurrentUser()).thenReturn(admin);

        //when
        Resource result = resourceService.getResourceById(resourceId);

        //then
        assertNotNull(result);
        assertEquals(resourceId, result.getId());
    }

    @Test
    void getResourceById_shouldThrowAccessDenied_whenUnpublishedAndNotAdmin() {
        //given
        UUID resourceId = UUID.randomUUID();

        ResourceEntity entity = new ResourceEntity();
        entity.setId(resourceId);
        entity.setPublished(false);

        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setPublished(false);

        User user = new User();
        user.setRole(Role.USER);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(entity));
        when(resourceMapper.toDomain(entity)).thenReturn(resource);
        when(userService.findCurrentUser()).thenReturn(user);

        //when & then
        assertThrows(AccessDeniedException.class,
                () -> resourceService.getResourceById(resourceId));
    }

    @Test
    void getResourceById_shouldThrowException_whenNotFound() {
        //given
        UUID resourceId = UUID.randomUUID();

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(ResourceNotFoundException.class,
                () -> resourceService.getResourceById(resourceId));
    }
}
