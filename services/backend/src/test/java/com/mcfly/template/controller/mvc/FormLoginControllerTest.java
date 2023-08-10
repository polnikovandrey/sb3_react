package com.mcfly.template.controller.mvc;

import com.mcfly.template.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ComponentScan(basePackages = "com.mcfly.template.security")
@Import(SecurityConfig.class)
@WebMvcTest(FormLoginController.class)
public class FormLoginControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Test
    void rootPageRedirectsToLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    void loginPageViewResolved() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.view().name("login"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
