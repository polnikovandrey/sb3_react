package com.mcfly.poll.controller.rest.user_role;

import com.mcfly.poll.domain.user_role.RoleName;
import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.payload.PagedResponse;
import com.mcfly.poll.payload.user_role.UpdateUserDataRequest;
import com.mcfly.poll.payload.user_role.UserDataResponse;
import com.mcfly.poll.payload.user_role.UserIdentityAvailability;
import com.mcfly.poll.security.CurrentUser;
import com.mcfly.poll.security.UserPrincipal;
import com.mcfly.poll.service.UserService;
import com.mcfly.poll.util.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDataResponse getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return userService.getCurrentUserData(currentUser);
    }

    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return userService.checkUsernameAvailability(username);
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        return userService.checkEmailAvailability(email);
    }

    @GetMapping("/{id}")
    public UserDataResponse getUserData(@PathVariable(value = "id") Long id) {
        return userService.getUserData(id);
    }

    // TODO test
    @PutMapping("/{id}")
    public UserDataResponse updateUserData(@PathVariable(value = "id") Long id, @Valid @RequestBody UpdateUserDataRequest updateUserDataRequest) {
        final String firstName = updateUserDataRequest.getFirstName();
        final String lastName = updateUserDataRequest.getLastName();
        final String middleName = updateUserDataRequest.getMiddleName();
        final String email = updateUserDataRequest.getEmail();
        final String name = updateUserDataRequest.getName();
        final String password = updateUserDataRequest.getPassword();
        return userService.updateUserData(id, firstName, lastName, middleName, email, name, password);
    }

    // TODO test
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
    }

    // TODO test
    @GetMapping({"/list", "/list/{pageIndex}"})
    public PagedResponse<UserDataResponse> listUsers(@PathVariable(required = false) Optional<Integer> pageIndex) {
        final Page<User> users = userService.getUsersPage(pageIndex.orElse(AppConstants.DEFAULT_PAGE_INDEX), AppConstants.DEFAULT_PAGE_SIZE);
        final List<UserDataResponse> responses
                = users.stream()
                .map(user -> new UserDataResponse(user.getId(),
                                                  user.getEmail(),
                                                  user.getUsername(),
                                                  user.getFirstName(),
                                                  user.getLastName(),
                                                  user.getMiddleName(),
                                                  user.getRoles().stream().anyMatch(role -> RoleName.ROLE_ADMIN == role.getName()))).toList();
        return new PagedResponse<>(responses, users.getNumber(), users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
    }
}
