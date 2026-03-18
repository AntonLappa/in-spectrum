package antony.lappa.inspectrum.controller.dto.resource;


import antony.lappa.inspectrum.controller.dto.ResourceAudience;
import antony.lappa.inspectrum.controller.dto.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUpdateRequestDto {

    private UUID id;
    private String title;
    private String description;
    private ResourceType type;
    private ResourceAudience audience;
    private String url;
    private String content;
    private Boolean published;

}
