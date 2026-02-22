package antony.lappa.inspectrum.service.user;

import antony.lappa.inspectrum.controller.dto.user.AdminUpdateUserRequestDto;
import antony.lappa.inspectrum.controller.dto.user.UserUpdateRequestDto;
import antony.lappa.inspectrum.service.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User findCurrentUser();

    User findById(UUID id);

    User findByEmail(String email);

    List<User> findAll();

    User update(UUID id, UserUpdateRequestDto request);

    User updateAdmin(UUID id, AdminUpdateUserRequestDto request);

    void deleteById(UUID id);
}
