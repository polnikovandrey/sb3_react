package com.mcfly.poll.controller.rest.user_role;

import com.mcfly.poll.payload.user_role.UpdateUserDataRequest;
import com.mcfly.poll.payload.user_role.UserDataResponse;
import com.mcfly.poll.payload.user_role.UserIdentityAvailability;
import com.mcfly.poll.security.CurrentUser;
import com.mcfly.poll.security.UserPrincipal;
import com.mcfly.poll.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

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
}
