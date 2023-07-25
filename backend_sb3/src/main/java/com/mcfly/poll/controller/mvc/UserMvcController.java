package com.mcfly.poll.controller.mvc;

import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.payload.polling.PagedResponse;
import com.mcfly.poll.payload.user_role.AddUserRequest;
import com.mcfly.poll.payload.user_role.EditUserFormData;
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
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class UserMvcController {

    private static final int USERS_PER_PAGE = 10;

    private final UserService userService;

    @GetMapping({"/list", "/list/{pageIndex}"})
    public String listUsers(@PathVariable(required = false) Optional<Integer> pageIndex, Model model) {
        final PagedResponse<UserResponse> users = userService.listUsersPage(pageIndex.orElse(0), USERS_PER_PAGE);
        model.addAttribute("users", users);
        return "listUsers";
    }

    @GetMapping("/add")
    public String showUserForm() {
        return "addUser";
    }

    @PostMapping("/add")
    public String postUserForm(@Valid @ModelAttribute AddUserRequest request) {
        userService.registerUser(request.getFirstName(), request.getLastName(), request.getMiddleName(), request.getUsername(), request.getEmail(), request.getPassword(), request.isAdmin());
        final int lastPageIndex = userService.getLastPageIndex(USERS_PER_PAGE);
        return "redirect:/user/list/" + lastPageIndex;
    }

    @GetMapping("/delete/{userId}/{pageIndex}")
    public String deleteUser(@PathVariable Long userId, @PathVariable Integer pageIndex) {
        userService.deleteUserById(userId);
        return "redirect:/user/list/" + pageIndex;
    }

    @GetMapping("/edit/{userId}/{pageIndex}")
    public String showEditUserForm(@PathVariable Long userId, @PathVariable Integer pageIndex, Model model) {
        final User user = userService.findUserById(userId);
        final EditUserFormData editUserData
                = EditUserFormData.builder()
                                  .userId(user.getId())
                                  .firstName(user.getFirstName())
                                  .lastName(user.getLastName())
                                  .middleName(user.getMiddleName())
                                  .pageIndex(pageIndex)
                                  .build();
        model.addAttribute("editUserFormData", editUserData);
        return "editUser";
    }

    @PostMapping("/edit")
    public String showEditUserForm(@Valid @ModelAttribute EditUserFormData editUserFormData) {
        userService.editUser(editUserFormData.getUserId(), editUserFormData.getFirstName(), editUserFormData.getLastName(), editUserFormData.getMiddleName());
        final int pageIndex = editUserFormData.getPageIndex();
        return "redirect:/user/list/" + pageIndex;
    }
}
