package com.mcfly.template.controller.rest;

import com.mcfly.template.config.SecurityConfig;
import com.mcfly.template.controller.rest.user_role.UserController;
import com.mcfly.template.exception.ResourceNotFoundException;
import com.mcfly.template.payload.user_role.UserDataResponse;
import com.mcfly.template.payload.user_role.UserIdentityAvailability;
import com.mcfly.template.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

@ComponentScan(basePackages = "com.mcfly.template.security")
@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void contextLoads() {
        assertThat(mockMvc).isNotNull();
        assertThat(userService).isNotNull();
    }

    @Test
    public void checkUsernameAvailabilityUnauthorizedAllowed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkUsernameAvailability").param("username", RandomStringUtils.randomAlphabetic(5)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void checkExistingUsernameAvailability() throws Exception {
        final String fakeExistingUsername = "Fake existing user";
        Mockito.when(userService.checkUsernameAvailability(fakeExistingUsername)).thenReturn(new UserIdentityAvailability(false));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkUsernameAvailability").param("username", fakeExistingUsername))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(false));
    }

    @Test
    public void checkAbsentUsernameAvailability() throws Exception {
        final String fakeAbsentUsername = "Fake absent user";
        Mockito.when(userService.checkUsernameAvailability(fakeAbsentUsername)).thenReturn(new UserIdentityAvailability(true));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkUsernameAvailability").param("username", fakeAbsentUsername))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));
    }

    @Test
    public void checkEmailAvailabilityUnauthorizedAllowed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkEmailAvailability").param("email", RandomStringUtils.randomAlphabetic(5)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void checkExistingEmailAvailability() throws Exception {
        final String fakeExistingEmail = "fake.existing@email.com";
        Mockito.when(userService.checkEmailAvailability(fakeExistingEmail)).thenReturn(new UserIdentityAvailability(false));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkEmailAvailability").param("email", fakeExistingEmail))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(false));
    }

    @Test
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
        final UserDataResponse userDataResponse = new UserDataResponse(1L, "email", "username", "firstName", "lastName", "middleName", true);
        Mockito.when(userService.getCurrentUserData(Mockito.any())).thenReturn(userDataResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/me"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userDataResponse.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDataResponse.getEmail()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userDataResponse.getName()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(userDataResponse.getFirstName()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(userDataResponse.getLastName()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.middleName").value(userDataResponse.getMiddleName()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.admin").value(userDataResponse.isAdmin()));
    }

    @Test
    public void checkGetUserDataUnauthorizedSecured() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", Long.toString(1L)))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="fakeUsername")
    public void checkGetExistingUserData() throws Exception {
        final UserDataResponse fakeUserDataResponse = new UserDataResponse(1L, "email", "name", "firstName", "lastName", "middleName", true);
        Mockito.when(userService.getUserData(fakeUserDataResponse.getId())).thenReturn(fakeUserDataResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", fakeUserDataResponse.getId()))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(fakeUserDataResponse.getId()));
    }

    @Test
    @WithMockUser(username="fakeUsername")
    public void checkGetAbsentUserData() throws Exception {
        final Long fakeAbsentUserId = -1L;
        Mockito.when(userService.getUserData(fakeAbsentUserId)).thenThrow(new ResourceNotFoundException("User not found", "User", "id", fakeAbsentUserId));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", fakeAbsentUserId))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
