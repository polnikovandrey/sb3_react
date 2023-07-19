package com.mcfly.poll.controller.user_role;

import com.mcfly.poll.payload.polling.PagedResponse;
import com.mcfly.poll.payload.polling.PollResponse;
import com.mcfly.poll.payload.polling.PollingUserProfile;
import com.mcfly.poll.security.CurrentUser;
import com.mcfly.poll.security.UserPrincipal;
import com.mcfly.poll.service.PollService;
import com.mcfly.poll.service.UserService;
import com.mcfly.poll.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    UserService userService;
    @Autowired
    PollService pollService;

    @GetMapping("/{username}")
    public PollingUserProfile getUserProfile(@PathVariable(value = "username") String username) {
        return userService.getUserProfile(username);
    }

    @GetMapping("/{username}/polls")
    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsCreatedBy(username, currentUser, page, size);
    }

    @GetMapping("/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsVotedBy(username, currentUser, page, size);
    }
}
