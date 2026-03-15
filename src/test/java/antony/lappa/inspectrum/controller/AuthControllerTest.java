package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.service.auth.AuthService;
import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.model.UserType;
import antony.lappa.inspectrum.service.token.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void signUp_shouldReturn201() throws Exception {
        //given
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setRole(Role.USER);

        when(authService.signUp(eq("John Doe"), eq("+380123456789"), eq("john@example.com"),
                eq("password123"), eq(UserType.PARENT)))
                .thenReturn(user);
        when(tokenService.createToken(userId.toString(), Role.USER)).thenReturn("jwt-token-value");

        String requestBody = """
                {
                    "name": "John Doe",
                    "phoneNumber": "+380123456789",
                    "email": "john@example.com",
                    "password": "password123",
                    "userType": "PARENT"
                }
                """;

        //when & then
        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token-value"));
    }

    @Test
    void login_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setRole(Role.USER);

        when(authService.login("john@example.com", "password123")).thenReturn(user);
        when(tokenService.createToken(userId.toString(), Role.USER)).thenReturn("jwt-token-value");

        String requestBody = """
                {
                    "email": "john@example.com",
                    "password": "password123"
                }
                """;

        //when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-value"));
    }
}
