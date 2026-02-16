package antony.lappa.inspectrum.service.user;

import antony.lappa.inspectrum.controller.dto.UserUpdateRequest;
import antony.lappa.inspectrum.service.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User findById(UUID id);

    User findByEmail(String email);

    List<User> findAll();

    User update(UUID id, UserUpdateRequest request);

    void deleteById(UUID id);
}
