package antony.lappa.inspectrum.repository;

import antony.lappa.inspectrum.repository.entity.AssessmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<AssessmentEntity, UUID> {

    Optional<AssessmentEntity> findFirstByUserIdOrderBySubmittedAtDesc(UUID userId);
}
