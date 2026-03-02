package antony.lappa.inspectrum.service.plan;

import antony.lappa.inspectrum.controller.dto.plan_item.PlanItemStatusUpdateRequestDto;
import antony.lappa.inspectrum.controller.dto.plans.PlanGenerateRequestDto;
import antony.lappa.inspectrum.exception.PlanNotFoundException;
import antony.lappa.inspectrum.mapper.PlanItemMapper;
import antony.lappa.inspectrum.mapper.PlanMapper;
import antony.lappa.inspectrum.repository.PlanItemRepository;
import antony.lappa.inspectrum.repository.PlanRepository;
import antony.lappa.inspectrum.repository.entity.PlanEntity;
import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import antony.lappa.inspectrum.service.model.Plan;
import antony.lappa.inspectrum.service.model.PlanItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanItemRepository planItemRepository;
    private final PlanMapper planMapper;
    private final PlanItemMapper planItemMapper;

    @Transactional
    @Override
    public Plan generate(UUID userId, PlanGenerateRequestDto request) {
        log.info("Generating plan for user {}", userId);

        PlanEntity plan = new PlanEntity();
        plan.setUserId(userId);
        plan.setAssessmentId(request.getAssessmentId());
        plan.setTitle("Personal plan");
        plan.setCreatedAt(Instant.now());

        PlanEntity savedPlan = planRepository.save(plan);
        log.info("Plan successfully created with id: {}", savedPlan.getId());

        List<PlanItemEntity> savedItems = Collections.emptyList();
        if (request.getResourceIds() != null && !request.getResourceIds().isEmpty()) {
            savedItems = IntStream.range(0, request.getResourceIds().size())
                    .mapToObj(index -> {
                        PlanItemEntity item = new PlanItemEntity();
                        item.setPlanId(savedPlan.getId());
                        item.setResourceId(request.getResourceIds().get(index));
                        item.setSortOrder(index);
                        return item;
                    }).toList();
            planItemRepository.saveAll(savedItems);
        }

        return planMapper.toDomain(savedPlan, savedItems);
    }

    @Override
    public Plan getCurrent(UUID userId) {
        log.info("Getting current plan for user {}", userId);

        PlanEntity planEntity = planRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new PlanNotFoundException("No plan for user " + userId));

        List<PlanItemEntity> items = planItemRepository.findAllByPlanIdOrderBySortOrderAsc(planEntity.getId());

        return planMapper.toDomain(planEntity, items);
    }

    @Override
    public PlanItem updateItemStatus(UUID userId, UUID itemId,
            PlanItemStatusUpdateRequestDto request) {
        log.info("Updating status for item {} to {}", itemId, request.getStatus());

        PlanItemEntity itemEntity = planItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with id " + itemId + " not found"));

        PlanEntity planEntity = planRepository.findById(itemEntity.getPlanId())
                .orElseThrow(() -> new PlanNotFoundException(itemEntity.getPlanId()));

        if (!planEntity.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User does not have access to this plan item");
        }

        itemEntity.setStatus(antony.lappa.inspectrum.repository.entity.Status.valueOf(request.getStatus().name()));
        PlanItemEntity savedItem = planItemRepository.save(itemEntity);

        return planItemMapper.toDomainItem(savedItem);
    }

    @Override
    public List<Plan> getAllPlans(UUID userId) {
        log.info("Getting all plans for user {}", userId);

        List<PlanEntity> plans = planRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        return plans.stream()
                .map(plan -> planMapper.toDomain(plan, Collections.emptyList()))
                .toList();
    }

}
