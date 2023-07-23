package com.mcfly.poll.controller.mvc.user;

import com.mcfly.poll.payload.polling.PagedResponse;
import com.mcfly.poll.payload.user_role.AddUserRequest;
import com.mcfly.poll.payload.user_role.UserResponse;
import com.mcfly.poll.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserMvcController {

    private static final int USERS_PER_PAGE = 10;

    private final UserService userService;

    @GetMapping({"/list", "/list/{page}"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String listUsers(@PathVariable(required = false) Optional<Integer> page, Model model) {
        final PagedResponse<UserResponse> users = userService.listUsersPage(page.orElse(0), USERS_PER_PAGE);
        model.addAttribute("users", users);
        return "listUsers";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showUserForm() {
        return "addUser";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postUserForm(@Valid @ModelAttribute AddUserRequest request) {
        userService.registerUser(request.getFirstName(), request.getLastName(), request.getMiddleName(), request.getUsername(), request.getEmail(), request.getPassword(), request.isAdmin());
        final int lastPageIndex = userService.getLastPageIndex(USERS_PER_PAGE);
        return "redirect:/user/list/" + lastPageIndex;
    }

    @GetMapping("/delete/{userId}/{pageId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable Long userId, @PathVariable Integer pageId) {
        userService.deleteUserById(userId);
        return "redirect:/user/list/" + pageId;
    }
}
