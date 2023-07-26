package com.mcfly.poll.controller.mvc;

import com.mcfly.poll.config.SecurityConfig;
import com.mcfly.poll.payload.polling.PagedResponse;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
}
