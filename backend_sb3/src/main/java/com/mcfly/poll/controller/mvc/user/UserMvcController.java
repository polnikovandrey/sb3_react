package com.mcfly.poll.controller.mvc.user;

import com.mcfly.poll.payload.polling.PagedResponse;
import com.mcfly.poll.payload.user_role.UserResponse;
import com.mcfly.poll.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserMvcController {

    private final UserService userService;

    @GetMapping({"/list", "/list/{page}"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String users(@PathVariable(required = false) Optional<Integer> page, Model model) {
        final PagedResponse<UserResponse> users = userService.findAllUsers(page.orElse(0), 10);
        model.addAttribute("users", users);
        return "users";
    }
}
