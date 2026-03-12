package antony.lappa.inspectrum.service.progress;

import antony.lappa.inspectrum.controller.dto.progress.ProgressCreateRequestDto;
import antony.lappa.inspectrum.exception.AccessDeniedException;
import antony.lappa.inspectrum.exception.DuplicateProgressEntryException;
import antony.lappa.inspectrum.exception.MissingRequiredFieldException;
import antony.lappa.inspectrum.exception.PlanItemNotFoundException;
import antony.lappa.inspectrum.exception.PlanNotFoundException;
import antony.lappa.inspectrum.mapper.ProgressMapper;
import antony.lappa.inspectrum.repository.PlanItemRepository;
import antony.lappa.inspectrum.repository.PlanRepository;
import antony.lappa.inspectrum.repository.ProgressRepository;
import antony.lappa.inspectrum.repository.entity.PlanEntity;
import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import antony.lappa.inspectrum.repository.entity.ProgressEntryEntity;
import antony.lappa.inspectrum.service.model.Progress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final ProgressMapper progressMapper;
    private final PlanItemRepository planItemRepository;
    private final PlanRepository planRepository;

    @Transactional
    @Override
    public Progress createProgressEntry(UUID userId, UUID planItemId, ProgressCreateRequestDto request) {
        log.info("Creating progress for user: {}, planItem: {}", userId, planItemId);

        validatePlanItemOwnership(userId, planItemId);

        ProgressCreateRequestDto effectiveRequest = request != null ? request : new ProgressCreateRequestDto();

        if (effectiveRequest.getEntryDate() == null) {
            throw new MissingRequiredFieldException("entry_date is required");
        }

        progressRepository.findByPlanItemIdAndEntryDate(planItemId, effectiveRequest.getEntryDate())
                .ifPresent(existing -> {
                    throw new DuplicateProgressEntryException(
                            "Progress entry already exists for planItem " + planItemId
                                    + " on date " + effectiveRequest.getEntryDate());
                });

        Progress progress = progressMapper.fromCreateRequest(effectiveRequest, planItemId);
        ProgressEntryEntity savedEntity = progressRepository.save(progressMapper.toEntity(progress));

        log.info("Progress entry created with id: {}", savedEntity.getId());
        return progressMapper.toDomain(savedEntity);
    }

    @Override
    public List<Progress> getProgressEntries(UUID userId, UUID planItemId) {
        log.info("Getting progress entries for user: {}, planItem: {}", userId, planItemId);

        validatePlanItemOwnership(userId, planItemId);

        return progressRepository.findByPlanItemIdOrderByEntryDateDesc(planItemId)
                .stream()
                .map(progressMapper::toDomain)
                .toList();
    }

    private void validatePlanItemOwnership(UUID userId, UUID planItemId) {
        PlanItemEntity planItemEntity = planItemRepository.findById(planItemId)
                .orElseThrow(() -> new PlanItemNotFoundException(planItemId));

        PlanEntity planEntity = planRepository.findById(planItemEntity.getPlanId())
                .orElseThrow(() -> new PlanNotFoundException(planItemEntity.getPlanId()));

        if (!planEntity.getUserId().equals(userId)) {
            throw new AccessDeniedException("User does not have access to this plan item");
        }
    }
}
