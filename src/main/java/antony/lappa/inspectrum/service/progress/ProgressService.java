package antony.lappa.inspectrum.service.progress;

import antony.lappa.inspectrum.controller.dto.progress.ProgressCreateRequestDto;
import antony.lappa.inspectrum.service.model.Progress;

import java.util.List;
import java.util.UUID;


public interface ProgressService {

    Progress createProgressEntry(UUID userId, UUID planItemId, ProgressCreateRequestDto progress);

    List<Progress> getProgressEntries(UUID userId, UUID planItemId);
}
