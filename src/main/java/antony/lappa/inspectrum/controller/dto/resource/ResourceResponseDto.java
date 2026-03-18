package antony.lappa.inspectrum.controller.dto.resource;


import antony.lappa.inspectrum.controller.dto.ResourceAudience;
import antony.lappa.inspectrum.controller.dto.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceResponseDto {

    private UUID id;
    private UUID skillId;
    private String title;
    private String description;
    private ResourceType type;
    private ResourceAudience audience;
    private String url;
    private String content;
    private Boolean published;
    private UUID createdBy;
    private Instant createdAt;
    private Instant updatedAt;

}
