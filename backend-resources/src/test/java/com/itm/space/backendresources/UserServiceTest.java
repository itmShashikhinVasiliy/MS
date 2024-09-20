package com.itm.space.backendresources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.space.backendresources.api.request.UserRequest;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserServiceTest extends BaseIntegrationTest {

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateUser() throws Exception {
        String username = "Test6";
        var user = new UserRequest( username, "test6@test.com", "pass", "FName", "LName");

        mvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        var itmUsers = keycloak.realm("ITM").users();
        String userId = itmUsers.search(username).get(0).getId();
        itmUsers.get(userId).remove();
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testGetUserById() throws Exception {
        String userId = "b905b94e-4768-4eaf-82c1-f1f4965bc722";

        mvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk());
    }
}
