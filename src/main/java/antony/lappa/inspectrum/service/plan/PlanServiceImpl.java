package antony.lappa.inspectrum.service.plan;

import antony.lappa.inspectrum.controller.dto.plan_item.PlanItemStatusUpdateRequestDto;
import antony.lappa.inspectrum.controller.dto.plans.PlanGenerateRequestDto;
import antony.lappa.inspectrum.exception.AccessDeniedException;
import antony.lappa.inspectrum.exception.AssessmentNotFoundException;
import antony.lappa.inspectrum.exception.AssessmentParseException;
import antony.lappa.inspectrum.exception.PlanNotFoundException;
import antony.lappa.inspectrum.exception.SkillNotFoundException;
import antony.lappa.inspectrum.mapper.PlanItemMapper;
import antony.lappa.inspectrum.mapper.PlanMapper;
import antony.lappa.inspectrum.repository.AssessmentRepository;
import antony.lappa.inspectrum.repository.PlanItemRepository;
import antony.lappa.inspectrum.repository.PlanRepository;
import antony.lappa.inspectrum.repository.ResourceRepository;
import antony.lappa.inspectrum.repository.SkillRepository;
import antony.lappa.inspectrum.repository.entity.AssessmentEntity;
import antony.lappa.inspectrum.repository.entity.PlanEntity;
import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import antony.lappa.inspectrum.repository.entity.ResourceEntity;
import antony.lappa.inspectrum.repository.entity.SkillEntity;
import antony.lappa.inspectrum.service.assessment.AssessmentTemplateService;
import antony.lappa.inspectrum.service.model.Plan;
import antony.lappa.inspectrum.service.model.PlanItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanItemRepository planItemRepository;
    private final AssessmentRepository assessmentRepository;
    private final SkillRepository skillRepository;
    private final ResourceRepository resourceRepository;
    private final PlanMapper planMapper;
    private final PlanItemMapper planItemMapper;
    private final AssessmentTemplateService assessmentTemplateService;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public Plan generate(UUID userId, PlanGenerateRequestDto request) {
        log.info("Generating plan for user {}", userId);

        AssessmentEntity assessment = assessmentRepository.findById(request.getAssessmentId())
                .orElseThrow(() -> new AssessmentNotFoundException(request.getAssessmentId()));

        if (!assessment.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access Denied");
        }

        Map<String, Integer> answers;
        try {
            answers = objectMapper.readValue(assessment.getAnswerJson(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new AssessmentParseException(assessment.getId(), e);
        }
        log.info("Parsed {} answers from assessment {}", answers.size(), assessment.getId());

        Map<String, Object> template = assessmentTemplateService.getTemplate();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> questions = (List<Map<String, Object>>) template.get("questions");

        if (questions == null) {
            throw new IllegalStateException("Template does not contain questions");
        }

        Map<String, String> questionCategoryMap = new HashMap<>();
        for (Map<String, Object> question : questions) {
            questionCategoryMap.put((String) question.get("id"), (String) question.get("category"));
        }
        log.info("Built questionCategoryMap with {} entries", questionCategoryMap.size());

        Map<String, Integer> categoryScores = new HashMap<>();
        for (Map.Entry<String, Integer> entry : answers.entrySet()) {
            String category = questionCategoryMap.get(entry.getKey());
            if (category != null) {
                categoryScores.merge(category, entry.getValue(), Integer::sum);
            }
        }
        log.info("Calculated categoryScores: {}", categoryScores);

        List<String> topCategories = categoryScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(2)
                .map(Map.Entry::getKey)
                .toList();
        log.info("Top categories: {}", topCategories);

        List<SkillEntity> selectedSkills = new ArrayList<>();
        for (String category : topCategories) {
            SkillEntity skill = skillRepository.findByCode(category)
                    .orElseThrow(() -> new SkillNotFoundException(category));
            selectedSkills.add(skill);
        }
        log.info("Selected {} skills for plan", selectedSkills.size());

        List<ResourceEntity> selectedResources = new ArrayList<>();
        for (SkillEntity skill : selectedSkills) {
            selectedResources.addAll(resourceRepository.findBySkillIdAndIsPublishedTrue(skill.getId()));
        }
        log.info("Selected {} resources for plan", selectedResources.size());

        PlanEntity plan = new PlanEntity();
        plan.setUserId(userId);
        plan.setAssessmentId(request.getAssessmentId());
        plan.setTitle("Personal plan");
        plan.setCreatedAt(Instant.now());

        PlanEntity savedPlan = planRepository.save(plan);
        log.info("Plan successfully created with id: {}", savedPlan.getId());

        List<PlanItemEntity> savedItems = Collections.emptyList();
        if (!selectedResources.isEmpty()) {
            List<PlanItemEntity> items = IntStream.range(0, selectedResources.size())
                    .mapToObj(index -> {
                        PlanItemEntity item = new PlanItemEntity();
                        item.setPlanId(savedPlan.getId());
                        item.setResourceId(selectedResources.get(index).getId());
                        item.setSortOrder(index);
                        return item;
                    }).toList();
            savedItems = planItemRepository.saveAll(items);
            log.info("Created {} plan items", savedItems.size());
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
