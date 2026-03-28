package antony.lappa.inspectrum.controller.dto.resource;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import antony.lappa.inspectrum.controller.dto.ResourceAudience;
import antony.lappa.inspectrum.controller.dto.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceCreateRequestDto {

    @JsonProperty("skill_id")
    @JsonAlias("skillId")
    private UUID skillId;
    @JsonProperty("resource_id")
    @JsonAlias("resourceId")
    private UUID resourceId;
    private String title;
    private String description;
    private ResourceType type;
    private ResourceAudience audience;
    private String url;
    private String content;
    private Boolean published;

}
