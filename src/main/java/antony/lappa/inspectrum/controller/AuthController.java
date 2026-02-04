package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.*;
import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.service.model.AuthService;
import antony.lappa.inspectrum.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.UUID;
import java.time.Instant;

@RestController
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @Autowired
    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }


    @PostMapping("/auth/sign-up")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {


        User user = authService.signUp(
                signUpRequestDto.getName(),
                signUpRequestDto.getPhoneNumber(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword(),
                signUpRequestDto.getUserType()
        );

        UserResponseDto responseDto = userMapper.toDto(user);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        User user = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        String fakeToken = "just-fake-jwt-for-check";

        return ResponseEntity.ok(new TokenResponseDto(fakeToken));
        }

}


