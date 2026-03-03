package antony.lappa.inspectrum.service.model;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class Resource {

    private UUID id;
    private UUID skillId;
    private String title;
    private String description;
    private ResourceType type;
    private ResourceAudience audience;
    private String url;
    private boolean published;
    private UUID createdBy;
    private Instant createdAt;
    private Instant updatedAt;

}
