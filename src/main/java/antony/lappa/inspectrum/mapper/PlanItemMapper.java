package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.plan_item.PlanItemResponseDto;
import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import antony.lappa.inspectrum.service.model.PlanItem;
import antony.lappa.inspectrum.service.model.Status;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlanItemMapper {

    private final ResourceMapper resourceMapper;

    public PlanItem toDomainItem(PlanItemEntity entity) {
        if (entity == null) {
            return null;
        }

        PlanItem item = new PlanItem();
        item.setId(entity.getId());
        item.setPlanId(entity.getPlanId());
        item.setResourceId(entity.getResourceId());
        item.setStatus(Status.valueOf(entity.getStatus().name()));
        item.setSortOrder(entity.getSortOrder());
        item.setCreatedAt(entity.getCreatedAt());

        return item;
    }

    public PlanItemResponseDto toDtoItem(PlanItem item) {
        if (item == null) {
            return null;
        }

        PlanItemResponseDto dto = new PlanItemResponseDto();
        dto.setId(item.getId());
        dto.setStatus(antony.lappa.inspectrum.controller.dto.Status.valueOf(item.getStatus().name()));
        dto.setSortOrder(item.getSortOrder());
        dto.setResource(resourceMapper.toShortDto(item.getResource()));

        return dto;
    }
}
