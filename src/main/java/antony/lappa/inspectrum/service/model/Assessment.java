package antony.lappa.inspectrum.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    private UUID id;
    private UUID userId;
    private Instant submittedAt;
    private Map<String, Object> answers;
    private Map<String, Object> result;
    private Instant createdAt;

}
