package antony.lappa.inspectrum.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assessments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssessmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Column(name = "answers_json", nullable = false, columnDefinition = "jsonb")
    private String answerJson;

    @Column(name = "result_json", nullable = false, columnDefinition = "jsonb")
    private String resultJson;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
        if (submittedAt == null) submittedAt = now;
    }
}
