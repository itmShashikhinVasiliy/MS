package com.itm.space.backendresources;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.List;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseIntegrationTest {

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "MODERATOR")
    void whenCreateUser_thenReturns201() throws Exception {
        var user = new UserRequest("tt", "tt@test.com", "password", "Fe", "Le");
        mvc.perform(requestWithContent(post("/api/users"), user))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void whenGetUserById_thenReturns200() throws Exception {
        UUID userId = UUID.randomUUID();
        UserResponse response = new UserResponse("fn", "ln", "tl@ex.com", List.of(), List.of());

        Mockito.when(userService.getUserById(userId))
                .thenReturn(response);

        mvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void whenGetHello_thenReturns200() throws Exception {
        mvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenUnauthorized_thenReturns403() throws Exception {
        mvc.perform(get("/api/users/hello"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void whenInvalidUserRequest_thenReturns400() throws Exception {
        UserRequest invalidRequest = new UserRequest("", "invalidemail", "", "", "");

        mvc.perform(requestWithContent(post("/api/users"), invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
