package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.plans.PlanResponseDto;
import antony.lappa.inspectrum.repository.entity.PlanEntity;
import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import antony.lappa.inspectrum.service.model.Plan;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PlanMapper {

    private final PlanItemMapper planItemMapper;

    public Plan toDomain(PlanEntity entity, List<PlanItemEntity> items) {
        Plan plan = new Plan();
        plan.setId(entity.getId());
        plan.setUserId(entity.getUserId());
        plan.setAssessmentId(entity.getAssessmentId());
        plan.setTitle(entity.getTitle());
        plan.setCreatedAt(entity.getCreatedAt());

        if (items != null) {
            plan.setItems(items.stream()
                    .map(planItemMapper::toDomainItem)
                    .collect(Collectors.toList()));
        }

        return plan;
    }

    public PlanResponseDto toDto(Plan plan) {
        if (plan == null) {
            return null;
        }

        PlanResponseDto dto = new PlanResponseDto();
        dto.setId(plan.getId());
        dto.setTitle(plan.getTitle());
        dto.setCreatedAt(plan.getCreatedAt());

        if (plan.getItems() != null) {
            dto.setItems(plan.getItems().stream()
                    .map(planItemMapper::toDtoItem)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

}