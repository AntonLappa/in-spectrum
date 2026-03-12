package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.progress.ProgressCreateRequestDto;
import antony.lappa.inspectrum.controller.dto.progress.ProgressEntryResponseDto;
import antony.lappa.inspectrum.repository.entity.ProgressEntryEntity;
import antony.lappa.inspectrum.service.model.Progress;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProgressMapper {

    public Progress toDomain(ProgressEntryEntity progressEntryEntity) {
        if (progressEntryEntity == null) {
            return null;
        }

        Progress progress = new Progress();
        progress.setId(progressEntryEntity.getId());
        progress.setPlanItemId(progressEntryEntity.getPlanItemId());
        progress.setEntryDate(progressEntryEntity.getEntryDate());
        progress.setNote(progressEntryEntity.getNote());
        progress.setCreatedAt(progressEntryEntity.getCreatedAt());
        return progress;
    }

    public ProgressEntryEntity toEntity(Progress progress) {
        if (progress == null) {
            return null;
        }

        ProgressEntryEntity progressEntity = new ProgressEntryEntity();
        progressEntity.setId(progress.getId());
        progressEntity.setPlanItemId(progress.getPlanItemId());
        progressEntity.setEntryDate(progress.getEntryDate());
        progressEntity.setNote(progress.getNote());
        return progressEntity;
    }

    public ProgressEntryResponseDto toDto(Progress progress) {
        if (progress == null) {
            return null;
        }

        ProgressEntryResponseDto dto = new ProgressEntryResponseDto();
        dto.setId(progress.getId());
        dto.setPlanItemId(progress.getPlanItemId());
        dto.setEntryDate(progress.getEntryDate());
        dto.setNote(progress.getNote());
        dto.setCreatedAt(progress.getCreatedAt());
        return dto;
    }

    public Progress fromCreateRequest(ProgressCreateRequestDto dto, UUID planItemId) {
        if (dto == null) {
            return null;
        }

        Progress progress = new Progress();
        progress.setPlanItemId(planItemId);
        progress.setEntryDate(dto.getEntryDate());
        progress.setNote(dto.getNote());
        return progress;
    }

}
