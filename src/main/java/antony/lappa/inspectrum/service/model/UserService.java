package antony.lappa.inspectrum.service.model;

import antony.lappa.inspectrum.controller.dto.UserUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {

    User findById(UUID id);

    User findByEmail(String email);

    List<User> findAll();

    User update(UUID id, UserUpdateRequest request);

    void deleteById(UUID id);
}
