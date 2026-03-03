package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.resource.ResourceResponseDto;
import antony.lappa.inspectrum.mapper.ResourceMapper;
import antony.lappa.inspectrum.service.model.Resource;
import antony.lappa.inspectrum.service.model.ResourceAudience;
import antony.lappa.inspectrum.service.model.ResourceType;
import antony.lappa.inspectrum.service.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final ResourceMapper resourceMapper;

    @GetMapping
    public ResponseEntity<List<ResourceResponseDto>> listResources(
            @RequestParam(required = false) ResourceType type,
            @RequestParam(required = false) ResourceAudience audience,
            @RequestParam(required = false) UUID skillId,
            @RequestParam(required = false) Boolean isPublished,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        List<Resource> resources = resourceService.listResource(type, audience, skillId, isPublished, limit, offset);

        List<ResourceResponseDto> response = resources.stream()
                .map(resourceMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<ResourceResponseDto> getResourceById(@PathVariable UUID resourceId) {
        Resource resource = resourceService.getResourceById(resourceId);
        return ResponseEntity.ok(resourceMapper.toResponseDto(resource));
    }

}
