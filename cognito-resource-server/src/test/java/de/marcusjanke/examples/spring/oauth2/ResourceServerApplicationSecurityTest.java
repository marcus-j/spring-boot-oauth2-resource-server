package de.marcusjanke.examples.spring.oauth2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
class ResourceServerApplicationSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    @DisplayName("Given an anonymous user, when a secured endpoint without pre-authorization is accessed, it returns status 401")
    void shouldDenyAccessToAnonymousUserOnEndpointWithoutPreAuthorization() throws Exception {
        mockMvc
                .perform(get("/authenticated"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "John")
    @DisplayName("Given an authenticated user without authorities, when a secured endpoint without pre-authorization is accessed, it returns status 200 and its message")
    void shouldProvideAccessToAuthenticatedUserWithoutAuthoritiesOnEndpointWithoutPreAuthorization() throws Exception {
        mockMvc
                .perform(get("/authenticated"))
                .andExpect(status().isOk())
                .andExpect(content().string("You're authenticated, John!"));
    }

    @Test
    @WithMockUser(value = "John")
    @DisplayName("Given an authenticated user without authorities, when a secured endpoint with pre-authorization is accessed, it returns status 403")
    void shouldDenyAccessToAuthenticatedUserWithoutRolesOnEndpointWithPreAuthorization() throws Exception {
        mockMvc
                .perform(get("/hello"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "John", authorities = "SCOPE_my-hello-resource/hello.read")
    @DisplayName("Given an authenticated user with correct authorities, when a secured endpoint with pre-authorization is accessed, it returns status 200 and its message")
    void shouldProvideAccessToAuthenticatedUserWithRolesOnEndpointWithoutPreAuthorization() throws Exception {
        mockMvc
                .perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello John"));
    }

    @Test
    @WithMockUser(value = "John", authorities = "SCOPE_my-hello-resource/hello.read")
    @DisplayName("Given an authenticated user with incorrect authorities, when a secured endpoint with pre-authorization is accessed, it returns status 403")
    void shouldDenyAccessToAuthenticatedUserWithWrongRolesOnEndpointWithPreAuthorization() throws Exception {
        mockMvc
                .perform(get("/user-info"))
                .andExpect(status().isForbidden());
    }
}