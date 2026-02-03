package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.UUID;
import java.time.Instant;

@RestController
public class AuthController {

    private HashMap<UUID, UserResponseDto> users = new HashMap<>();

    @PostMapping("/auth/sign-up")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {


        UserResponseDto response = new UserResponseDto();
        response.setName(signUpRequestDto.getName());
        response.setPhoneNumber(signUpRequestDto.getPhoneNumber());
        response.setEmail(signUpRequestDto.getEmail());
        response.setUserType(signUpRequestDto.getUserType());
        response.setId(UUID.randomUUID());
        response.setRole(Role.USER);
        response.setActive(true);
        response.setCreatedAt(Instant.now());

        users.put(response.getId(), response);

        return ResponseEntity.status(201).body(response);

    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        UserResponseDto user = users.values()
                .stream()
                .filter(u ->  u.getEmail().equals(loginRequestDto.getEmail()))
                .findFirst()
                .orElse(null);

        String fakeToken = "just-fake-jwt-for-check";

        return ResponseEntity.ok(new TokenResponseDto(fakeToken));
        }
    }


}
