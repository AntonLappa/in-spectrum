package antony.lappa.inspectrum.repository;

import antony.lappa.inspectrum.repository.entity.ProgressEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgressRepository extends JpaRepository<ProgressEntryEntity, UUID> {

    Optional<ProgressEntryEntity> findByPlanItemIdAndEntryDate(UUID planItemId, LocalDate entryDate);

    List<ProgressEntryEntity> findByPlanItemIdOrderByEntryDateDesc(UUID planItemId);
}
