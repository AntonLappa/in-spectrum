package antony.lappa.inspectrum.service.token;

import antony.lappa.inspectrum.service.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceImplTest {

    private TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl();
        ReflectionTestUtils.setField(tokenService, "jwtSecret",
                "super-secret-key-that-is-at-least-32-bytes-long-for-hmac-sha256");
        ReflectionTestUtils.setField(tokenService, "jwtTtlMillis", 3600000L);
    }

    @Test
    void createToken_shouldReturnValidJwt() {
        //given
        String id = UUID.randomUUID().toString();
        Role role = Role.USER;

        //when
        String token = tokenService.createToken(id, role);

        //then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void isValidToken_shouldReturnTrue_forValidToken() {
        //given
        String id = UUID.randomUUID().toString();
        String token = tokenService.createToken(id, Role.USER);

        //when
        boolean result = tokenService.isValidToken(token);

        //then
        assertTrue(result);
    }

    @Test
    void isValidToken_shouldReturnFalse_forInvalidToken() {
        //given
        String invalidToken = "invalid.token.value";

        //when
        boolean result = tokenService.isValidToken(invalidToken);

        //then
        assertFalse(result);
    }

    @Test
    void getId_shouldReturnSubject() {
        //given
        String id = UUID.randomUUID().toString();
        String token = tokenService.createToken(id, Role.USER);

        //when
        String result = tokenService.getId(token);

        //then
        assertEquals(id, result);
    }

    @Test
    void getRole_shouldReturnRole() {
        //given
        String id = UUID.randomUUID().toString();
        Role role = Role.ADMIN;
        String token = tokenService.createToken(id, role);

        //when
        Role result = tokenService.getRole(token);

        //then
        assertEquals(Role.ADMIN, result);
    }
}
