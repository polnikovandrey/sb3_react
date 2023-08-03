package com.mcfly.poll.controller.rest.user_role;

import com.mcfly.poll.payload.user_role.UserDataResponse;
import com.mcfly.poll.payload.user_role.UserIdentityAvailability;
import com.mcfly.poll.payload.user_role.UserSummary;
import com.mcfly.poll.security.CurrentUser;
import com.mcfly.poll.security.UserPrincipal;
import com.mcfly.poll.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/me")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return userService.getUserSummary(currentUser);
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
}
