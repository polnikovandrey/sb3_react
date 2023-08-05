package com.mcfly.poll.repository;

import com.mcfly.poll.domain.user_role.Role;
import com.mcfly.poll.domain.user_role.RoleName;
import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.exception.AppException;
import com.mcfly.poll.repository.user_role.RoleRepository;
import com.mcfly.poll.repository.user_role.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void testFindRoleByName() {
        final Optional<Role> roleUserOptional = roleRepository.findByName(RoleName.ROLE_USER);
        assertTrue(roleUserOptional.isPresent());
        final Optional<Role> roleAdminOptional = roleRepository.findByName(RoleName.ROLE_ADMIN);
        assertTrue(roleAdminOptional.isPresent());
    }

    @Test
    void testPersistUserWithNotValidEmailFails() {
        final User user = createRandomUser();
        user.setEmail("not valid email");
        assertThrows(ConstraintViolationException.class, () -> userRepository.saveAndFlush(user));
    }

    @Test
    void testExistsUserByUsername() {
        final User user = persistRandomUser();
        final Boolean existsByUsername = userRepository.existsByUsername(user.getUsername());
        assertTrue(existsByUsername);
    }

    @Test
    void testExistsUserByEmail() {
        final User user = persistRandomUser();
        final Boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
        assertTrue(existsByEmail);
    }

    @Test
    void testFindUserByUsernameOrEmail() {
        final User user = persistRandomUser();
        final Optional<User> userByUsername = userRepository.findByUsernameOrEmail(user.getUsername(), null);
        assertTrue(userByUsername.isPresent());
        final Optional<User> userByEmail = userRepository.findByUsernameOrEmail(null, user.getEmail());
        assertTrue(userByEmail.isPresent());
    }

    @Test
    void testFindUserByIdIn() {
        final User user1 = persistRandomUser();
        final User user2 = persistRandomUser();
        final List<User> foundUsers = userRepository.findByIdIn(List.of(user1.getId(), user2.getId()));
        assertThat(foundUsers).isNotNull().isNotEmpty().size().isEqualTo(2);
    }

    private User persistRandomUser() {
        final User user = createRandomUser();
        return userRepository.saveAndFlush(user);
    }

    private User createRandomUser() {
        final String firstName = RandomStringUtils.randomAlphabetic(5);
        final String lastName = RandomStringUtils.randomAlphabetic(5);
        final String middleName = RandomStringUtils.randomAlphabetic(5);
        final String username = RandomStringUtils.randomAlphabetic(5);
        final String email = RandomStringUtils.randomAlphabetic(5) + "@gmail.com";
        final String password = RandomStringUtils.randomAlphabetic(5);
        final User user =
                User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .middleName(middleName)
                        .username(username)
                        .email(email)
                        .password(password)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build();
        final Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
        return user;
    }
}
