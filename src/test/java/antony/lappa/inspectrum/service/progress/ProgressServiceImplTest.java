package antony.lappa.inspectrum.service.progress;

import antony.lappa.inspectrum.controller.dto.progress.ProgressCreateRequestDto;
import antony.lappa.inspectrum.exception.AccessDeniedException;
import antony.lappa.inspectrum.exception.DuplicateProgressEntryException;
import antony.lappa.inspectrum.exception.PlanItemNotFoundException;
import antony.lappa.inspectrum.mapper.ProgressMapper;
import antony.lappa.inspectrum.repository.PlanItemRepository;
import antony.lappa.inspectrum.repository.PlanRepository;
import antony.lappa.inspectrum.repository.ProgressRepository;
import antony.lappa.inspectrum.repository.entity.PlanEntity;
import antony.lappa.inspectrum.repository.entity.PlanItemEntity;
import antony.lappa.inspectrum.repository.entity.ProgressEntryEntity;
import antony.lappa.inspectrum.service.model.Progress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceImplTest {

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private ProgressMapper progressMapper;

    @Mock
    private PlanItemRepository planItemRepository;

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private ProgressServiceImpl progressService;

    @Test
    void createProgressEntry_shouldCreateAndReturn() {
        //given
        UUID userId = UUID.randomUUID();
        UUID planItemId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();

        ProgressCreateRequestDto request = new ProgressCreateRequestDto();
        request.setEntryDate(LocalDate.of(2026, 3, 15));
        request.setNote("Test note");

        PlanItemEntity planItemEntity = new PlanItemEntity();
        planItemEntity.setId(planItemId);
        planItemEntity.setPlanId(planId);

        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(planId);
        planEntity.setUserId(userId);

        Progress progress = new Progress();
        progress.setPlanItemId(planItemId);

        ProgressEntryEntity savedEntity = new ProgressEntryEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setPlanItemId(planItemId);

        Progress expectedProgress = new Progress();
        expectedProgress.setId(savedEntity.getId());

        when(planItemRepository.findById(planItemId)).thenReturn(Optional.of(planItemEntity));
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));
        when(progressRepository.findByPlanItemIdAndEntryDate(planItemId, request.getEntryDate()))
                .thenReturn(Optional.empty());
        when(progressMapper.fromCreateRequest(request, planItemId)).thenReturn(progress);
        when(progressMapper.toEntity(progress)).thenReturn(new ProgressEntryEntity());
        when(progressRepository.save(any(ProgressEntryEntity.class))).thenReturn(savedEntity);
        when(progressMapper.toDomain(savedEntity)).thenReturn(expectedProgress);

        //when
        Progress result = progressService.createProgressEntry(userId, planItemId, request);

        //then
        assertNotNull(result);
        assertEquals(savedEntity.getId(), result.getId());
        verify(progressRepository).save(any(ProgressEntryEntity.class));
    }

    @Test
    void createProgressEntry_shouldThrowException_whenDuplicate() {
        //given
        UUID userId = UUID.randomUUID();
        UUID planItemId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();

        ProgressCreateRequestDto request = new ProgressCreateRequestDto();
        request.setEntryDate(LocalDate.of(2026, 3, 15));

        PlanItemEntity planItemEntity = new PlanItemEntity();
        planItemEntity.setId(planItemId);
        planItemEntity.setPlanId(planId);

        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(planId);
        planEntity.setUserId(userId);

        when(planItemRepository.findById(planItemId)).thenReturn(Optional.of(planItemEntity));
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));
        when(progressRepository.findByPlanItemIdAndEntryDate(planItemId, request.getEntryDate()))
                .thenReturn(Optional.of(new ProgressEntryEntity()));

        //when & then
        assertThrows(DuplicateProgressEntryException.class,
                () -> progressService.createProgressEntry(userId, planItemId, request));
    }

    @Test
    void createProgressEntry_shouldThrowException_whenPlanItemNotFound() {
        //given
        UUID userId = UUID.randomUUID();
        UUID planItemId = UUID.randomUUID();

        when(planItemRepository.findById(planItemId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(PlanItemNotFoundException.class,
                () -> progressService.createProgressEntry(userId, planItemId, new ProgressCreateRequestDto()));
    }

    @Test
    void createProgressEntry_shouldThrowException_whenUserDoesNotOwnPlan() {
        //given
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID planItemId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();

        PlanItemEntity planItemEntity = new PlanItemEntity();
        planItemEntity.setId(planItemId);
        planItemEntity.setPlanId(planId);

        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(planId);
        planEntity.setUserId(otherUserId);

        when(planItemRepository.findById(planItemId)).thenReturn(Optional.of(planItemEntity));
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));

        //when & then
        assertThrows(AccessDeniedException.class,
                () -> progressService.createProgressEntry(userId, planItemId, new ProgressCreateRequestDto()));
    }

    @Test
    void getProgressEntries_shouldReturnEntries() {
        //given
        UUID userId = UUID.randomUUID();
        UUID planItemId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();

        PlanItemEntity planItemEntity = new PlanItemEntity();
        planItemEntity.setId(planItemId);
        planItemEntity.setPlanId(planId);

        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(planId);
        planEntity.setUserId(userId);

        ProgressEntryEntity entry1 = new ProgressEntryEntity();
        entry1.setId(UUID.randomUUID());
        ProgressEntryEntity entry2 = new ProgressEntryEntity();
        entry2.setId(UUID.randomUUID());

        Progress progress1 = new Progress();
        Progress progress2 = new Progress();

        when(planItemRepository.findById(planItemId)).thenReturn(Optional.of(planItemEntity));
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));
        when(progressRepository.findByPlanItemIdOrderByEntryDateDesc(planItemId))
                .thenReturn(List.of(entry1, entry2));
        when(progressMapper.toDomain(entry1)).thenReturn(progress1);
        when(progressMapper.toDomain(entry2)).thenReturn(progress2);

        //when
        List<Progress> result = progressService.getProgressEntries(userId, planItemId);

        //then
        assertEquals(2, result.size());
    }
}
