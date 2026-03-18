package antony.lappa.inspectrum.service.resource;

import antony.lappa.inspectrum.service.model.Resource;
import antony.lappa.inspectrum.service.model.ResourceAudience;
import antony.lappa.inspectrum.service.model.ResourceType;

import java.util.List;
import java.util.UUID;

public interface ResourceService {

    List<Resource> listResource(ResourceType type,
                                ResourceAudience audience,
                                UUID skillId,
                                Boolean isPublished,
                                int limit,
                                int offset);

    Resource getResourceById(UUID resourceId);

    Resource createResource(Resource resource);
}
