package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.AdminUpdateUserRequestDto;
import antony.lappa.inspectrum.controller.dto.UserResponseDto;
import antony.lappa.inspectrum.controller.dto.UserUpdateRequestDto;
import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Secured("ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @Secured("ADMIN")
    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @Secured("ADMIN")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        User user = userService.findCurrentUser();
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateCurrentUser(@RequestBody UserUpdateRequestDto request) {
        User currentUser = userService.findCurrentUser();
        User updatedUser = userService.update(currentUser.getId(), request);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @Secured("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id,
            @RequestBody AdminUpdateUserRequestDto request) {
        User updatedUser = userService.updateAdmin(id, request);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @Secured("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
