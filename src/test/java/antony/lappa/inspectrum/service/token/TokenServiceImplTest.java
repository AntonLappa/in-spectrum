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
                "ThisIsATestSecretKeyThatMustBeAtLeast256BitsLong!");
        ReflectionTestUtils.setField(tokenService, "jwtTtlMillis", 3600000L);
    }

    @Test
    void createToken_ShouldReturnValidJwt() {
        String userId = UUID.randomUUID().toString();

        String token = tokenService.createToken(userId, Role.USER);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void isValidToken_ShouldReturnTrue_WhenTokenValid() {
        String userId = UUID.randomUUID().toString();
        String token = tokenService.createToken(userId, Role.USER);

        assertTrue(tokenService.isValidToken(token));
    }

    @Test
    void isValidToken_ShouldReturnFalse_WhenTokenInvalid() {
        assertFalse(tokenService.isValidToken("invalid.token.value"));
    }

    @Test
    void getId_ShouldReturnSubject() {
        String userId = UUID.randomUUID().toString();
        String token = tokenService.createToken(userId, Role.USER);

        String result = tokenService.getId(token);

        assertEquals(userId, result);
    }

    @Test
    void getRole_ShouldReturnRole() {
        String userId = UUID.randomUUID().toString();
        String token = tokenService.createToken(userId, Role.ADMIN);

        Role result = tokenService.getRole(token);

        assertEquals(Role.ADMIN, result);
    }
}
