package antony.lappa.inspectrum.controller.dto.resource;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("skill_id")
    @JsonAlias("skillId")
    private UUID skillId;
    private String title;
    private String description;
    private ResourceType type;
    private ResourceAudience audience;
    private String url;
    private String content;
    private Boolean published;
    @JsonProperty("created_by")
    @JsonAlias("createdBy")
    private UUID createdBy;
    @JsonProperty("created_at")
    @JsonAlias("createdAt")
    private Instant createdAt;
    @JsonProperty("updated_at")
    @JsonAlias("updatedAt")
    private Instant updatedAt;

}
