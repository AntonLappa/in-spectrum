package antony.lappa.inspectrum.repository;

import antony.lappa.inspectrum.repository.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, UUID> {

    Optional<PlanEntity> findFirstByUserIdOrderByCreatedAtDesc(UUID userID);

    List<PlanEntity> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
