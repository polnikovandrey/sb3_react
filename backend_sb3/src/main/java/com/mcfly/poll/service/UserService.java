package com.mcfly.poll.service;

import com.mcfly.poll.domain.user_role.Role;
import com.mcfly.poll.domain.user_role.RoleName;
import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.exception.AppException;
import com.mcfly.poll.exception.ResourceNotFoundException;
import com.mcfly.poll.exception.UserExistsAlreadyException;
import com.mcfly.poll.payload.polling.PagedResponse;
import com.mcfly.poll.payload.polling.PollingUserProfile;
import com.mcfly.poll.payload.user_role.UserIdentityAvailability;
import com.mcfly.poll.payload.user_role.UserResponse;
import com.mcfly.poll.payload.user_role.UserSummary;
import com.mcfly.poll.repository.polling.PollRepository;
import com.mcfly.poll.repository.polling.VoteRepository;
import com.mcfly.poll.repository.user_role.RoleRepository;
import com.mcfly.poll.repository.user_role.UserRepository;
import com.mcfly.poll.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;

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

    public PagedResponse<UserResponse> listUsersPage(int page, int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        final Page<User> users = userRepository.findAll(pageable);
        final List<UserResponse> responses
                = users.stream().map(user -> UserResponse.builder().id(user.getId()).username(user.getUsername())
                .email(user.getEmail()).firstName(user.getFirstName())
                .lastName(user.getLastName()).middleName(user.getMiddleName())
                .roles(user.getRoles().stream().map(role -> role.getName().getName()).collect(Collectors.toSet())).build()).toList();
        return new PagedResponse<>(responses, users.getNumber(), users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
    }

    public int getLastPageIndex(int size) {
        final int usersCount = (int) userRepository.count();
        final boolean strict = usersCount % size == 0;
        final int chunks = usersCount / size;
        return strict ? chunks - 1 : chunks;
    }

    @Transactional
    public User registerUser(String firstName, String lastName, String middleName, String username, String email, String password, boolean admin) {
        if (userRepository.existsByUsername(username)) {
            throw new UserExistsAlreadyException("Username is already taken!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserExistsAlreadyException("Email Address is already in use");
        }
        final RoleName roleName = admin ? RoleName.ROLE_ADMIN : RoleName.ROLE_USER;
        final Role userRole = roleRepository.findByName(roleName).orElseThrow(() -> new AppException("User Role not set."));
        final User user = User.builder().firstName(firstName).lastName(lastName).middleName(middleName).username(username).email(email).password(passwordEncoder.encode(password))
                .roles(Collections.singleton(userRole)).build();
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void editUser(Long id, String firstName, String lastName, String middleName) {
        final User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        userRepository.save(user);
    }
}
