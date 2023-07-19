package com.mcfly.poll.service;

import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.exception.ResourceNotFoundException;
import com.mcfly.poll.payload.polling.PollingUserProfile;
import com.mcfly.poll.payload.user_role.UserIdentityAvailability;
import com.mcfly.poll.payload.user_role.UserSummary;
import com.mcfly.poll.repository.polling.PollRepository;
import com.mcfly.poll.repository.polling.VoteRepository;
import com.mcfly.poll.repository.user_role.UserRepository;
import com.mcfly.poll.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PollRepository pollRepository;

    @Autowired
    VoteRepository voteRepository;

    public UserSummary getUserSummary(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
    }

    public UserIdentityAvailability checkUsernameAvailability(String username) {
        final Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    public UserIdentityAvailability checkEmailAvailability(String email) {
        final Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    public PollingUserProfile getUserProfile(String username) {
        final User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found", "User", "username", username));
        final long pollCount = pollRepository.countByCreatedBy(user.getId());
        final long voteCount = voteRepository.countByUserId(user.getId());
        return new PollingUserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount);
    }
}
