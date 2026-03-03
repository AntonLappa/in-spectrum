package antony.lappa.inspectrum.repository.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resources")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "skill_id", nullable = false)
    private UUID skillId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Column(name = "description")
    private String description;

    @Column(name = "audience")
    @Enumerated(EnumType.STRING)
    private ResourceAudience audience;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "is_published", nullable = false)
    private boolean isPublished;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
