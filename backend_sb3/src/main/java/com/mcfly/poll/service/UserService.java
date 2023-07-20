package com.mcfly.poll.service;

import com.mcfly.poll.domain.user_role.Role;
import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.exception.ResourceNotFoundException;
import com.mcfly.poll.payload.polling.PagedResponse;
import com.mcfly.poll.payload.polling.PollingUserProfile;
import com.mcfly.poll.payload.user_role.UserIdentityAvailability;
import com.mcfly.poll.payload.user_role.UserResponse;
import com.mcfly.poll.payload.user_role.UserSummary;
import com.mcfly.poll.repository.polling.PollRepository;
import com.mcfly.poll.repository.polling.VoteRepository;
import com.mcfly.poll.repository.user_role.UserRepository;
import com.mcfly.poll.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PollRepository pollRepository;

    @Autowired
    VoteRepository voteRepository;

    public UserSummary getUserSummary(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getMiddleName());
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
        return new PollingUserProfile(user.getId(), user.getUsername(), user.getLastName(), user.getCreatedAt(), pollCount, voteCount);
    }

    public PagedResponse<UserResponse> findAllUsers(int page, int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        final Page<User> users = userRepository.findAll(pageable);
        final List<UserResponse> responses
                = users.stream().map(user -> UserResponse.builder().id(user.getId()).username(user.getUsername())
                .email(user.getEmail()).firstName(user.getFirstName())
                .lastName(user.getLastName()).middleName(user.getMiddleName())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())).build()).toList();
        return new PagedResponse<>(responses, users.getNumber(), users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
    }
}
