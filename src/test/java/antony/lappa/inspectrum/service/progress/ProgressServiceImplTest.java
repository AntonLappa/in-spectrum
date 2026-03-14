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
import org.junit.jupiter.api.BeforeEach;
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

    private UUID userId;
    private UUID planItemId;
    private UUID planId;
    private PlanItemEntity planItemEntity;
    private PlanEntity planEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        planItemId = UUID.randomUUID();
        planId = UUID.randomUUID();

        planItemEntity = new PlanItemEntity();
        planItemEntity.setId(planItemId);
        planItemEntity.setPlanId(planId);

        planEntity = new PlanEntity();
        planEntity.setId(planId);
        planEntity.setUserId(userId);
    }

    @Test
    void createProgressEntry_ShouldSave_WhenValid() {
        ProgressCreateRequestDto request = new ProgressCreateRequestDto();
        request.setEntryDate(LocalDate.now());
        request.setNote("Good progress");

        Progress progress = new Progress();
        ProgressEntryEntity savedEntity = new ProgressEntryEntity();
        savedEntity.setId(UUID.randomUUID());

        when(planItemRepository.findById(planItemId)).thenReturn(Optional.of(planItemEntity));
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));
        when(progressRepository.findByPlanItemIdAndEntryDate(planItemId, request.getEntryDate()))
                .thenReturn(Optional.empty());
        when(progressMapper.fromCreateRequest(request, planItemId)).thenReturn(progress);
        when(progressMapper.toEntity(progress)).thenReturn(savedEntity);
        when(progressRepository.save(savedEntity)).thenReturn(savedEntity);
        when(progressMapper.toDomain(savedEntity)).thenReturn(progress);

        Progress result = progressService.createProgressEntry(userId, planItemId, request);

        assertNotNull(result);
        verify(progressRepository).save(savedEntity);
    }

    @Test
    void createProgressEntry_ShouldThrowDuplicate_WhenEntryExists() {
        ProgressCreateRequestDto request = new ProgressCreateRequestDto();
        request.setEntryDate(LocalDate.now());

        ProgressEntryEntity existingEntry = new ProgressEntryEntity();

        when(planItemRepository.findById(planItemId)).thenReturn(Optional.of(planItemEntity));
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));
        when(progressRepository.findByPlanItemIdAndEntryDate(planItemId, request.getEntryDate()))
                .thenReturn(Optional.of(existingEntry));

        assertThrows(DuplicateProgressEntryException.class,
                () -> progressService.createProgressEntry(userId, planItemId, request));

        verify(progressRepository, never()).save(any());
    }

    @Test
    void createProgressEntry_ShouldThrowException_WhenPlanItemNotFound() {
        ProgressCreateRequestDto request = new ProgressCreateRequestDto();
        request.setEntryDate(LocalDate.now());

        when(planItemRepository.findById(planItemId)).thenReturn(Optional.empty());

        assertThrows(PlanItemNotFoundException.class,
                () -> progressService.createProgressEntry(userId, planItemId, request));
    }

    @Test
    void createProgressEntry_ShouldThrowAccessDenied_WhenNotOwner() {
        UUID otherUserId = UUID.randomUUID();
        ProgressCreateRequestDto request = new ProgressCreateRequestDto();
        request.setEntryDate(LocalDate.now());

        when(planItemRepository.findById(planItemId)).thenReturn(Optional.of(planItemEntity));
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));

        assertThrows(AccessDeniedException.class,
                () -> progressService.createProgressEntry(otherUserId, planItemId, request));
    }

    @Test
    void getProgressEntries_ShouldReturnEntries_WhenOwned() {
        ProgressEntryEntity entry1 = new ProgressEntryEntity();
        ProgressEntryEntity entry2 = new ProgressEntryEntity();
        Progress progress1 = new Progress();
        Progress progress2 = new Progress();

        when(planItemRepository.findById(planItemId)).thenReturn(Optional.of(planItemEntity));
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));
        when(progressRepository.findByPlanItemIdOrderByEntryDateDesc(planItemId))
                .thenReturn(List.of(entry1, entry2));
        when(progressMapper.toDomain(entry1)).thenReturn(progress1);
        when(progressMapper.toDomain(entry2)).thenReturn(progress2);

        List<Progress> result = progressService.getProgressEntries(userId, planItemId);

        assertEquals(2, result.size());
    }
}
