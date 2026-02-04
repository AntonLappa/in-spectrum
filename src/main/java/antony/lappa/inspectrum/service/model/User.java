package antony.lappa.inspectrum.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class User {

    private UUID id;
    private String name;
    private String phoneNumber;
    private String email;
    private UserType userType;
    private Role role;
    private boolean active;
    private Instant createdAt;
}
