package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.user.UserResponseDto;
import antony.lappa.inspectrum.mapper.UserMapper;
import antony.lappa.inspectrum.service.model.Role;
import antony.lappa.inspectrum.service.model.User;
import antony.lappa.inspectrum.service.model.UserType;
import antony.lappa.inspectrum.service.token.TokenService;
import antony.lappa.inspectrum.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void getUserById_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        UserResponseDto dto = new UserResponseDto();
        dto.setId(userId);
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setUserType(UserType.PARENT);
        dto.setRole(Role.USER);
        dto.setActive(true);

        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        //when & then
        mockMvc.perform(get("/users/{id}", userId))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getAllUsers_shouldReturn200() throws Exception {
        //given
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        User user2 = new User();
        user2.setId(UUID.randomUUID());

        UserResponseDto dto1 = new UserResponseDto();
        dto1.setId(user1.getId());
        UserResponseDto dto2 = new UserResponseDto();
        dto2.setId(user2.getId());

        when(userService.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        //when & then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getCurrentUser_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setName("Current User");

        UserResponseDto dto = new UserResponseDto();
        dto.setId(userId);
        dto.setName("Current User");

        when(userService.findCurrentUser()).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        //when & then
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Current User"));
    }

    @Test
    void updateCurrentUser_shouldReturn200() throws Exception {
        //given
        UUID userId = UUID.randomUUID();

        User currentUser = new User();
        currentUser.setId(userId);

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Updated Name");

        UserResponseDto dto = new UserResponseDto();
        dto.setId(userId);
        dto.setName("Updated Name");

        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(userService.update(eq(userId), any())).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(dto);

        String requestBody = """
                {
                    "name": "Updated Name",
                    "phone_number": "+380111111111"
                }
                """;

        //when & then
        mockMvc.perform(put("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void deleteUser_shouldReturn204() throws Exception {
        //given
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).deleteById(userId);

        //when & then
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(userId);
    }
}
