package antony.lappa.inspectrum.mapper;

import antony.lappa.inspectrum.controller.dto.progress.ProgressCreateRequestDto;
import antony.lappa.inspectrum.controller.dto.progress.ProgressEntryResponseDto;
import antony.lappa.inspectrum.repository.entity.ProgressEntryEntity;
import antony.lappa.inspectrum.service.model.Progress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProgressMapperTest {

    private ProgressMapper progressMapper;

    @BeforeEach
    void setUp() {
        progressMapper = new ProgressMapper();
    }

    @Test
    void toDomain_shouldMapEntityToDomain() {
        //given
        ProgressEntryEntity entity = new ProgressEntryEntity();
        entity.setId(UUID.randomUUID());
        entity.setPlanItemId(UUID.randomUUID());
        entity.setEntryDate(LocalDate.of(2026, 3, 15));
        entity.setNote("Test note");
        entity.setCreatedAt(Instant.now());

        //when
        Progress domain = progressMapper.toDomain(entity);

        //then
        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getPlanItemId(), domain.getPlanItemId());
        assertEquals(entity.getEntryDate(), domain.getEntryDate());
        assertEquals(entity.getNote(), domain.getNote());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
    }

    @Test
    void toDomain_shouldReturnNull_whenEntityIsNull() {
        //given
        ProgressEntryEntity entity = null;

        //when
        Progress domain = progressMapper.toDomain(entity);

        //then
        assertNull(domain);
    }

    @Test
    void toEntity_shouldMapDomainToEntity() {
        //given
        Progress progress = new Progress();
        progress.setId(UUID.randomUUID());
        progress.setPlanItemId(UUID.randomUUID());
        progress.setEntryDate(LocalDate.of(2026, 3, 15));
        progress.setNote("Test note");

        //when
        ProgressEntryEntity entity = progressMapper.toEntity(progress);

        //then
        assertNotNull(entity);
        assertEquals(progress.getId(), entity.getId());
        assertEquals(progress.getPlanItemId(), entity.getPlanItemId());
        assertEquals(progress.getEntryDate(), entity.getEntryDate());
        assertEquals(progress.getNote(), entity.getNote());
    }

    @Test
    void toEntity_shouldReturnNull_whenDomainIsNull() {
        //given
        Progress progress = null;

        //when
        ProgressEntryEntity entity = progressMapper.toEntity(progress);

        //then
        assertNull(entity);
    }

    @Test
    void toDto_shouldMapDomainToDto() {
        //given
        Progress progress = new Progress();
        progress.setId(UUID.randomUUID());
        progress.setPlanItemId(UUID.randomUUID());
        progress.setEntryDate(LocalDate.of(2026, 3, 15));
        progress.setNote("Test note");
        progress.setCreatedAt(Instant.now());

        //when
        ProgressEntryResponseDto dto = progressMapper.toDto(progress);

        //then
        assertNotNull(dto);
        assertEquals(progress.getId(), dto.getId());
        assertEquals(progress.getPlanItemId(), dto.getPlanItemId());
        assertEquals(progress.getEntryDate(), dto.getEntryDate());
        assertEquals(progress.getNote(), dto.getNote());
        assertEquals(progress.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    void toDto_shouldReturnNull_whenDomainIsNull() {
        //given
        Progress progress = null;

        //when
        ProgressEntryResponseDto dto = progressMapper.toDto(progress);

        //then
        assertNull(dto);
    }

    @Test
    void fromCreateRequest_shouldMapDtoToDomain() {
        //given
        UUID planItemId = UUID.randomUUID();
        ProgressCreateRequestDto dto = new ProgressCreateRequestDto();
        dto.setEntryDate(LocalDate.of(2026, 3, 15));
        dto.setNote("New note");

        //when
        Progress domain = progressMapper.fromCreateRequest(dto, planItemId);

        //then
        assertNotNull(domain);
        assertEquals(planItemId, domain.getPlanItemId());
        assertEquals(dto.getEntryDate(), domain.getEntryDate());
        assertEquals(dto.getNote(), domain.getNote());
    }

    @Test
    void fromCreateRequest_shouldReturnNull_whenDtoIsNull() {
        //given
        ProgressCreateRequestDto dto = null;
        UUID planItemId = UUID.randomUUID();

        //when
        Progress domain = progressMapper.fromCreateRequest(dto, planItemId);

        //then
        assertNull(domain);
    }
}
