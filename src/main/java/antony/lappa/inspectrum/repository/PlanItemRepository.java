package antony.lappa.inspectrum.repository;

import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlanItemRepository extends JpaRepository<PlanItemEntity, UUID> {

    List<PlanItemEntity> findAllByPlanIdOrderBySortOrderAsc(UUID id);
}
