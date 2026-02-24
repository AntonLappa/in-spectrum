package antony.lappa.inspectrum.service.plan;

import antony.lappa.inspectrum.controller.dto.plans.PlanGenerateRequestDto;
import antony.lappa.inspectrum.controller.dto.plans.PlanItemStatusUpdateRequestDto;
import antony.lappa.inspectrum.service.model.Plan;
import antony.lappa.inspectrum.service.model.PlanItem;

import java.util.List;
import java.util.UUID;

public interface PlanService {

    Plan getCurrent(UUID userId);

    Plan generate(UUID userId, PlanGenerateRequestDto request);

    PlanItem updateItemStatus(UUID userId, UUID itemId, PlanItemStatusUpdateRequestDto request);

    List<Plan> getAllPlans(UUID userId);
}
