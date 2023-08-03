package com.mcfly.poll.controller.mvc;

import com.mcfly.poll.config.SecurityConfig;
import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.payload.PagedResponse;
import com.mcfly.poll.payload.user_role.EditUserFormData;
import com.mcfly.poll.payload.user_role.UserResponse;
import com.mcfly.poll.security.CustomUserDetailsService;
import com.mcfly.poll.security.JwtAuthenticationFilter;
import com.mcfly.poll.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Collections;

@WebMvcTest(UserMvcController.class)
@Import(SecurityConfig.class)
public class UserMvcControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    CustomUserDetailsService customUserDetailsService;
    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void userListDeniedUnauthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/list"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
               .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void userListDeniedUserAccess() throws Exception {
        final PagedResponse<UserResponse> responseStub = new PagedResponse<>(Collections.emptyList(), 0, 0, 0, 0, true);
        Mockito.when(userService.listUsersPage(Mockito.anyInt(), Mockito.anyInt()))
               .thenReturn(responseStub);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/list"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void userList() throws Exception {
        final PagedResponse<UserResponse> expected = new PagedResponse<>(Collections.emptyList(), 0, 0, 0, 0, true);
        Mockito.when(userService.listUsersPage(Mockito.anyInt(), Mockito.anyInt()))
               .thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/list"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void showAddUserFormDeniedUnauthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/add"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
               .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void showAddUserFormDeniedUserAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/add"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showAddUserForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/add"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("addUser"));
    }

    @Test
    public void postAddUserFormDeniedUnauthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/add"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void postAddUserFormDeniedUserAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/add"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void postAddUserForm() throws Exception {
        final int expectedLastPageIndex = 1;
        Mockito.when(userService.getLastPageIndex(Mockito.anyInt())).thenReturn(expectedLastPageIndex);
        mockMvc.perform(MockMvcRequestBuilders.post("/user/add")
                                              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                              .param("firstName", "First Name")
                                              .param("lastName", "Last Name")
                                              .param("middleName", "Middle Name")
                                              .param("username", "username")
                                              .param("email", "email@mail.com")
                                              .param("password", "password")
                                              .param("admin", "false")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
               .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list/" + expectedLastPageIndex));
        Mockito.verify(userService, Mockito.times(1)).registerUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
        Mockito.verify(userService, Mockito.times(1)).getLastPageIndex(Mockito.anyInt());
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUserDeniedUnauthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/42/0"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
               .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteUserDeniedUserAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/42/0"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteUser() throws Exception {
        final int expectedLastPageIndex = 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/42/" + expectedLastPageIndex))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
               .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list/" + expectedLastPageIndex));
        Mockito.verify(userService, Mockito.times(1)).deleteUserById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void showEditUserFormDeniedUnauthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/edit/42/0"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
               .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void showEditUserFormDeniedUserAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/edit/42/0"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showEditUserForm() throws Exception {
        final Long userId = 42L;
        final Integer pageIndex = 0;
        final User userStub = new User(userId, "username", "email", "password", "firstName", "lastName", "middleName",
                                    Collections.emptySet(), Instant.now(), Instant.now(), 0L);
        final EditUserFormData expectedEditUserFormData
                = EditUserFormData.builder()
                                  .userId(userStub.getId())
                                  .firstName(userStub.getFirstName())
                                  .lastName(userStub.getLastName())
                                  .middleName(userStub.getMiddleName())
                                  .pageIndex(pageIndex)
                                  .build();
        Mockito.when(userService.findUserById(userId)).thenReturn(userStub);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/edit/{userId}/{pageIndex}", userId, pageIndex))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.model().attribute("editUserFormData", expectedEditUserFormData))
               .andExpect(MockMvcResultMatchers.view().name("editUser"));
    }

    @Test
    public void editUserDeniedUnauthorizedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/edit")
                                              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                              .param("userId", "42")
                                              .param("firstName", "firstName")
                                              .param("lastName", "lastName")
                                              .param("middleName", "middleName")
                                              .param("pageIndex", "1")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
               .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void editUserDeniedUserAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/edit")
                                              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                              .param("userId", "42")
                                              .param("firstName", "firstName")
                                              .param("lastName", "lastName")
                                              .param("middleName", "middleName")
                                              .param("pageIndex", "1")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void editUser() throws Exception {
        final long userId = 42L;
        final String firstName = "firstName";
        final String lastName = "lastName";
        final String middleName = "middleName";
        final int pageIndex = 1;
        mockMvc.perform(MockMvcRequestBuilders.post("/user/edit")
                                              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                              .param("userId", Long.toString(userId))
                                              .param("firstName", firstName)
                                              .param("lastName", lastName)
                                              .param("middleName", middleName)
                                              .param("pageIndex", Integer.toString(pageIndex))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
               .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list/" + pageIndex));
        Mockito.verify(userService, Mockito.times(1)).updateUserData(userId, firstName, lastName, middleName);
        Mockito.verifyNoMoreInteractions(userService);
    }
}
