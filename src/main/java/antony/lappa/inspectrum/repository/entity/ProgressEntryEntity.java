package antony.lappa.inspectrum.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table (name = "progress_entries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgressEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "plan_item_id", nullable = false)
    private UUID planItemId;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
