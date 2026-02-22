package antony.lappa.inspectrum.controller.dto.resource;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceShortDto {

    private UUID id;

    private String title;

    private String type;

    private String audience;

    private String url;
}
