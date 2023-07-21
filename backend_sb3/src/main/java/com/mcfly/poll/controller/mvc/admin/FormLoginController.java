package com.mcfly.poll.controller.mvc.admin;

import com.mcfly.poll.payload.polling.PagedResponse;
import com.mcfly.poll.payload.user_role.UserResponse;
import com.mcfly.poll.security.JwtUtils;
import com.mcfly.poll.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class FormLoginController {

    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String logout(HttpServletResponse res) {
        res.setHeader("Authorization", null);
        return "redirect:/login";
    }

    @GetMapping("/users/{page}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String users(@PathVariable int page, Model model) {
        final PagedResponse<UserResponse> users = userService.findAllUsers(page, 10);
        model.addAttribute("users", users);
        return "users";
    }
}
