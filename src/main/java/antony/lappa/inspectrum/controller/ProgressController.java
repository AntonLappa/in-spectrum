package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.progress.ProgressCreateRequestDto;
import antony.lappa.inspectrum.controller.dto.progress.ProgressEntryResponseDto;
import antony.lappa.inspectrum.mapper.ProgressMapper;
import antony.lappa.inspectrum.service.model.Progress;
import antony.lappa.inspectrum.service.progress.ProgressService;
import antony.lappa.inspectrum.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/plan-items")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressMapper progressMapper;
    private final ProgressService progressService;
    private final UserService userService;

    @PostMapping("/{planItemId}/progress")
    public ResponseEntity<ProgressEntryResponseDto> createProgressEntry(
            @PathVariable UUID planItemId,
            @Valid @RequestBody(required = false) ProgressCreateRequestDto request) {

        UUID userId = userService.findCurrentUser().getId();
        Progress progress = progressService.createProgressEntry(userId, planItemId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(progressMapper.toDto(progress));
    }

    @GetMapping("/{planItemId}/progress")
    public ResponseEntity<List<ProgressEntryResponseDto>> getProgressEntries(@PathVariable UUID planItemId) {

        UUID userId = userService.findCurrentUser().getId();
        List<Progress> progressEntries = progressService.getProgressEntries(userId, planItemId);

        List<ProgressEntryResponseDto> response = progressEntries.stream()
                .map(progressMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }
}
