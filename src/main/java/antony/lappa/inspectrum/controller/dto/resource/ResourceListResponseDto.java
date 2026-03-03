package antony.lappa.inspectrum.controller.dto.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceListResponseDto {

    private long total;
    private int limit;
    private int offset;
    private List<ResourceListResponseDto> items;

}
