package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.*;
import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.service.token.TokenService;
import antony.lappa.inspectrum.service.auth.AuthService;
import antony.lappa.inspectrum.service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;



@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;
    private final TokenService tokenService;


    @PostMapping("/auth/sign-up")
    public ResponseEntity<TokenResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {

        User user = authService.signUp(
                signUpRequestDto.getName(),
                signUpRequestDto.getPhoneNumber(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword(),
                signUpRequestDto.getUserType());

        String token = tokenService.createToken(
                user.getId().toString(),
                user.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TokenResponseDto(token));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        User user = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        String token = tokenService.createToken(user.getId().toString(), user.getRole());

        return ResponseEntity.ok(new TokenResponseDto(token));
    }

}
