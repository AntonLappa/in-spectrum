package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.plans.PlanGenerateRequestDto;
import antony.lappa.inspectrum.controller.dto.plans.PlanResponseDto;
import antony.lappa.inspectrum.controller.dto.plan_item.PlanItemResponseDto;
import antony.lappa.inspectrum.controller.dto.plan_item.PlanItemStatusUpdateRequestDto;
import antony.lappa.inspectrum.mapper.PlanItemMapper;
import antony.lappa.inspectrum.mapper.PlanMapper;
import antony.lappa.inspectrum.service.model.Plan;
import antony.lappa.inspectrum.service.model.PlanItem;
import antony.lappa.inspectrum.service.plan.PlanService;
import antony.lappa.inspectrum.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanMapper planMapper;
    private final PlanItemMapper planItemMapper;
    private final UserService userService;
    private final PlanService planService;

    @PostMapping("/generate")
    public ResponseEntity<PlanResponseDto> generatePlan(@Valid @RequestBody PlanGenerateRequestDto request) {

        UUID userId = userService.findCurrentUser().getId();
        Plan plan = planService.generate(userId, request);
        return ResponseEntity.ok(planMapper.toDto(plan));
    }

    @GetMapping("/current")
    public ResponseEntity<PlanResponseDto> getCurrent() {

        UUID userId = userService.findCurrentUser().getId();
        Plan plan = planService.getCurrent(userId);
        return ResponseEntity.ok(planMapper.toDto(plan));

    }

    @GetMapping
    public ResponseEntity<List<PlanResponseDto>> getAllPlans() {
        UUID userId = userService.findCurrentUser().getId();
        List<Plan> plans = planService.getAllPlans(userId);

        List<PlanResponseDto> response = plans.stream()
                .map(planMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/items/{itemId}/status")
    public ResponseEntity<PlanItemResponseDto> updateItemStatus(
            @PathVariable UUID itemId,
            @RequestBody PlanItemStatusUpdateRequestDto request) {

        UUID userId = userService.findCurrentUser().getId();
        PlanItem updatedItem = planService.updateItemStatus(userId, itemId,
                request);

        return ResponseEntity.ok(planItemMapper.toDtoItem(updatedItem));
    }

}
