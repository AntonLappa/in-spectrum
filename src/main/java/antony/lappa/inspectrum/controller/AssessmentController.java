package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.AssessmentCreateRequestDto;
import antony.lappa.inspectrum.controller.dto.AssessmentResponseDto;
import antony.lappa.inspectrum.mapper.AssessmentMapper;
import antony.lappa.inspectrum.service.model.Assessment;
import antony.lappa.inspectrum.service.assessment.AssessmentService;
import antony.lappa.inspectrum.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final AssessmentMapper assessmentMapper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<AssessmentResponseDto> createAssessment(@RequestBody AssessmentCreateRequestDto request) {
        UUID userId = userService.findCurrentUser().getId();
        Assessment assessment = assessmentService.create(userId, request);
        return ResponseEntity.ok(assessmentMapper.toDto(assessment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssessmentResponseDto> getAssessmentById(@PathVariable UUID id) {
        UUID userId = userService.findCurrentUser().getId();
        Assessment assessment = assessmentService.getById(userId, id);
        return ResponseEntity.ok(assessmentMapper.toDto(assessment));
    }
}
