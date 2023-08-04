package com.mcfly.poll.service;

import com.mcfly.poll.domain.user_role.Role;
import com.mcfly.poll.domain.user_role.RoleName;
import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.exception.AppException;
import com.mcfly.poll.exception.ResourceNotFoundException;
import com.mcfly.poll.exception.UserExistsAlreadyException;
import com.mcfly.poll.payload.user_role.UserDataResponse;
import com.mcfly.poll.payload.user_role.UserIdentityAvailability;
import com.mcfly.poll.repository.user_role.RoleRepository;
import com.mcfly.poll.repository.user_role.UserRepository;
import com.mcfly.poll.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataResponse getCurrentUserData(UserPrincipal currentUser) {
        final Long id = currentUser.getId();
        final String email = currentUser.getEmail();
        final String username = currentUser.getUsername();
        final String firstName = currentUser.getFirstName();
        final String lastName = currentUser.getLastName();
        final String middleName = currentUser.getMiddleName();
        final boolean admin = currentUser.getAuthorities().stream().anyMatch(grantedAuthority -> RoleName.ROLE_USER.getName().equals(grantedAuthority.getAuthority()));
        return new UserDataResponse(id, email, username, firstName, lastName, middleName, admin);
    }

    public UserIdentityAvailability checkUsernameAvailability(String username) {
        final Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    public UserIdentityAvailability checkEmailAvailability(String email) {
        final Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    public UserDataResponse getUserData(Long id) {
        final User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found", "User", "id", id));
        return new UserDataResponse(user.getId(), user.getEmail(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getMiddleName(),
                                    user.getRoles().stream().anyMatch(role -> RoleName.ROLE_ADMIN == role.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<User> getUsersPage(int page, int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        return userRepository.findAll(pageable);
    }

    public int getLastPageIndex(int size) {
        final int usersCount = (int) userRepository.count();
        if (usersCount == 0) {
            return 0;
        }
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
    public void updateUserData(Long id, String firstName, String lastName, String middleName) {
        updateUserData(id, firstName, lastName, middleName, null, null, null);
    }

    @Transactional
    public UserDataResponse updateUserData(Long id, String firstName, String lastName, String middleName, String email, String name, String password) {
        final User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        if (email != null) {
            user.setEmail(email);
        }
        if (name != null) {
            user.setUsername(name);
        }
        if (password != null) {
            user.setPassword(passwordEncoder.encode(password));
        }
        final User updatedUser = userRepository.saveAndFlush(user);
        return new UserDataResponse(updatedUser.getId(), updatedUser.getEmail(), updatedUser.getUsername(),
                                    updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getMiddleName(),
                                    updatedUser.getRoles().stream().anyMatch(role -> RoleName.ROLE_ADMIN == role.getName()));
    }
}
