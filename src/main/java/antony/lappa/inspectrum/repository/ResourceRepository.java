package antony.lappa.inspectrum.repository;

import antony.lappa.inspectrum.repository.entity.ResourceAudience;
import antony.lappa.inspectrum.repository.entity.ResourceEntity;
import antony.lappa.inspectrum.repository.entity.ResourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, UUID> {

    @Override
    Optional<ResourceEntity> findById(UUID uuid);

    @Query("SELECT r FROM ResourceEntity r WHERE " +
            "(:type IS NULL OR r.type = :type) AND " +
            "(:audience IS NULL OR r.audience = :audience) AND " +
            "(:skillId IS NULL OR r.skillId = :skillId) AND " +
            "(:isPublished IS NULL OR r.isPublished = :isPublished)")
    Page<ResourceEntity> findResources(@Param("type") ResourceType type,
            @Param("audience") ResourceAudience audience,
            @Param("skillId") UUID skillId,
            @Param("isPublished") Boolean isPublished,
            Pageable pageable);
}
