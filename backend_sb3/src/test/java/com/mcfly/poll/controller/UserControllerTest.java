package com.mcfly.poll.controller;

import com.mcfly.poll.controller.user_role.UserController;
import com.mcfly.poll.payload.user_role.UserIdentityAvailability;
import com.mcfly.poll.payload.user_role.UserSummary;
import com.mcfly.poll.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserService userService;

    @Test
    void contextLoads() {
        assertThat(mockMvc).isNotNull();
        assertThat(userService).isNotNull();
    }

    @Test
    public void checkUsernameAvailabilityUnauthorizedSecured() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkUsernameAvailability").param("username", RandomStringUtils.randomAlphabetic(5)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="fakeUsername")
    public void checkExistingUsernameAvailability() throws Exception {
        final String fakeExistingUsername = "Fake existing user";
        Mockito.when(userService.checkUsernameAvailability(fakeExistingUsername)).thenReturn(new UserIdentityAvailability(false));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkUsernameAvailability").param("username", fakeExistingUsername))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(false));
    }

    @Test
    @WithMockUser(username="fakeUsername")
    public void checkAbsentUsernameAvailability() throws Exception {
        final String fakeAbsentUsername = "Fake absent user";
        Mockito.when(userService.checkUsernameAvailability(fakeAbsentUsername)).thenReturn(new UserIdentityAvailability(true));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkUsernameAvailability").param("username", fakeAbsentUsername))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));
    }

    @Test
    public void checkEmailAvailabilityUnauthorizedSecured() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkEmailAvailability").param("email", RandomStringUtils.randomAlphabetic(5)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="fakeUsername")
    public void checkExistingEmailAvailability() throws Exception {
        final String fakeExistingEmail = "fake.existing@email.com";
        Mockito.when(userService.checkEmailAvailability(fakeExistingEmail)).thenReturn(new UserIdentityAvailability(false));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkEmailAvailability").param("email", fakeExistingEmail))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(false));
    }

    @Test
    @WithMockUser(username="fakeUsername")
    public void checkAbsentEmailAvailability() throws Exception {
        final String fakeAbsentEmail = "fake.absent@email.com";
        Mockito.when(userService.checkEmailAvailability(fakeAbsentEmail)).thenReturn(new UserIdentityAvailability(true));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkEmailAvailability").param("email", fakeAbsentEmail))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));
    }

    @Test
    public void checkMeUnauthorizedSecured() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/me"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "fakeUsername")
    public void checkMe() throws Exception {
        final UserSummary userSummary = new UserSummary(1L, "username", "firstName", "lastName", "middleName");
        //noinspection DataFlowIssue
        Mockito.when(userService.getUserSummary(null)).thenReturn(userSummary);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/me"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("firstName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("lastName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.middleName").value("middleName"));
    }
}
