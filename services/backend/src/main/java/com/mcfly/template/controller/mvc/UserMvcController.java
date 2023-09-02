package com.mcfly.template.controller.mvc;

import com.mcfly.template.domain.user_role.User;
import com.mcfly.template.payload.PagedResponse;
import com.mcfly.template.payload.user_role.AddUserRequest;
import com.mcfly.template.payload.user_role.EditUserFormData;
import com.mcfly.template.payload.user_role.UserResponse;
import com.mcfly.template.service.UserService;
import com.mcfly.template.util.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserMvcController {

    private static final int USERS_PER_PAGE = 10;

    private final UserService userService;

    @GetMapping({"/list", "/list/{pageIndex}"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String listUsers(@PathVariable(required = false) Optional<Integer> pageIndex, Model model) {
        final Page<User> users = userService.getUsersPage(pageIndex.orElse(AppConstants.DEFAULT_PAGE_INDEX), AppConstants.DEFAULT_PAGE_SIZE);
        final List<UserResponse> responses
                = users.stream()
                       .map(user -> UserResponse.builder().id(user.getId()).username(user.getUsername())
                                                .email(user.getEmail()).firstName(user.getFirstName())
                                                .lastName(user.getLastName()).middleName(user.getMiddleName())
                                                .emailConfirmed(user.isEmailConfirmed())
                                                .roles(user.getRoles().stream().map(role -> role.getName().getName()).collect(Collectors.toSet())).build()).toList();
        final PagedResponse<UserResponse> usersPageResponse = new PagedResponse<>(responses, users.getNumber(), users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
        model.addAttribute("users", usersPageResponse);
        return "listUsers";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showUserForm() {
        return "addUser";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postUserForm(@Valid @ModelAttribute AddUserRequest addUserRequest) {
        userService.registerUser(addUserRequest.getFirstName(),
                                 addUserRequest.getLastName(),
                                 addUserRequest.getMiddleName(),
                                 addUserRequest.getUsername(),
                                 addUserRequest.getEmail(),
                                 addUserRequest.getPassword(),
                                 addUserRequest.isAdmin());
        final int lastPageIndex = userService.getLastPageIndex(USERS_PER_PAGE);
        return "redirect:/user/list/" + lastPageIndex;
    }

    @GetMapping("/delete/{userId}/{pageIndex}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable Long userId, @PathVariable Integer pageIndex) {
        userService.deleteUserById(userId);
        return "redirect:/user/list/" + pageIndex;
    }

    @GetMapping("/edit/{userId}/{pageIndex}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editUser(@Valid @ModelAttribute EditUserFormData editUserFormData) {
        userService.updateUserData(editUserFormData.getUserId(), editUserFormData.getFirstName(), editUserFormData.getLastName(), editUserFormData.getMiddleName());
        final int pageIndex = editUserFormData.getPageIndex();
        return "redirect:/user/list/" + pageIndex;
    }

    @GetMapping("/confirmEmail")
    public String confirmEmail(@RequestParam String email, @RequestParam String code, Model model) {
        userService.validateEmailConfirmationCode(email, code);
        model.addAttribute("email", email);
        return "emailConfirmationSuccess";
    }
}
