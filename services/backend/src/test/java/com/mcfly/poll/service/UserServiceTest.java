package com.mcfly.poll.service;

import com.mcfly.poll.domain.user_role.Role;
import com.mcfly.poll.domain.user_role.RoleName;
import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.exception.ResourceNotFoundException;
import com.mcfly.poll.exception.UserExistsAlreadyException;
import com.mcfly.poll.payload.user_role.UserDataResponse;
import com.mcfly.poll.payload.user_role.UserIdentityAvailability;
import com.mcfly.poll.repository.user_role.RoleRepository;
import com.mcfly.poll.repository.user_role.UserRepository;
import com.mcfly.poll.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    void contextLoads() {
        assertThat(userService).isNotNull();
    }

    @Test
    void userSummary() {
        final UserService userServiceMock = Mockito.mock();
        final UserDataResponse fakeUserDataResponse = new UserDataResponse(1L, "email", "username", "firstName", "lastName", "middleName", true);
        Mockito.when(userServiceMock.getCurrentUserData(Mockito.any(UserPrincipal.class)))
                .thenReturn(fakeUserDataResponse);
        final UserDataResponse userDataResponse = userServiceMock.getCurrentUserData(new UserPrincipal());
        assertThat(userDataResponse).usingRecursiveComparison().isEqualTo(fakeUserDataResponse);
    }

    @Test
    void checkEmailAvailability() {
        final String existingEmail = "existing@email.com";
        final String absentEmail = "absent@email.com";
        Mockito.when(userRepository.existsByEmail(existingEmail))
                .thenReturn(Boolean.TRUE);
        Mockito.when(userRepository.existsByEmail(absentEmail))
                .thenReturn(Boolean.FALSE);
        final UserIdentityAvailability actualExisting = userService.checkEmailAvailability(existingEmail);
        assertThat(actualExisting.getAvailable()).isEqualTo(Boolean.FALSE);
        final UserIdentityAvailability actualAbsent = userService.checkEmailAvailability(absentEmail);
        assertThat(actualAbsent.getAvailable()).isEqualTo(Boolean.TRUE);
        Mockito.verify(userRepository, Mockito.times(2)).existsByEmail(Mockito.any());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getAbsentUserProfileThrowsException() {
        final Long absentUserId = -1L;
        Mockito.when(userRepository.findById(absentUserId))
                .thenThrow(new ResourceNotFoundException("User not found", "User", "id", absentUserId));
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserData(absentUserId));
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getExistingUserProfile() {
        final Long existingUserId = 1L;
        final User expectedUser =
                User.builder()
                        .id(existingUserId)
                        .username("username")
                        .email("user@email.com")
                        .password("password")
                        .firstName("firstName")
                        .lastName("lastName")
                        .middleName("middleName")
                        .roles(Set.of(new Role(RoleName.ROLE_ADMIN)))
                        .build();
        final UserDataResponse expectedUserDataResponse
                = new UserDataResponse(expectedUser.getId(), expectedUser.getEmail(), expectedUser.getUsername(), expectedUser.getFirstName(), expectedUser.getLastName(), expectedUser.getMiddleName(), true);
        Mockito.when(userRepository.findById(existingUserId))
                .thenReturn(Optional.of(expectedUser));
        final UserDataResponse actual = userService.getUserData(existingUserId);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedUserDataResponse);
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void registerUserWithExistingUsernameFails() {
        Mockito.when(userRepository.existsByUsername(Mockito.any())).thenReturn(true);
        assertThrows(UserExistsAlreadyException.class,
                     () -> userService.registerUser("firstName", "lastName", "middleName", "username", "email@mail.com", "password", false));
    }

    @Test
    void registerUserWithExistingEmailFails() {
        Mockito.when(userRepository.existsByUsername(Mockito.any())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(Mockito.any())).thenReturn(true);
        assertThrows(UserExistsAlreadyException.class,
                     () -> userService.registerUser("firstName", "lastName", "middleName", "username", "email@mail.com", "password", false));
    }

    @Test
    void registerUserCorrectUserRole() {
        final Role roleMock = Mockito.mock(Role.class);
        final User userMock = Mockito.mock(User.class);
        Mockito.when(userRepository.existsByUsername(Mockito.any())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(Mockito.any())).thenReturn(false);
        Mockito.when(roleRepository.findByName(Mockito.any())).thenReturn(Optional.of(roleMock));
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("password");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userMock);
        final ArgumentCaptor<RoleName> roleNameArgumentCaptor = ArgumentCaptor.forClass(RoleName.class);
        userService.registerUser("firstName", "lastName", "middleName", "username", "email@mail.com", "password", false);
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(roleNameArgumentCaptor.capture());
        assertThat(roleNameArgumentCaptor.getValue()).isEqualTo(RoleName.ROLE_USER);
        userService.registerUser("firstName", "lastName", "middleName", "username", "email@mail.com", "password", true);
        Mockito.verify(roleRepository, Mockito.times(2)).findByName(roleNameArgumentCaptor.capture());
        assertThat(roleNameArgumentCaptor.getValue()).isEqualTo(RoleName.ROLE_ADMIN);
    }

    @Test
    void registerUserCorrectUser() {
        final String firstName = "firstName";
        final String lastName = "lastName";
        final String middleName = "middleName";
        final String username = "username";
        final String mail = "email@mail.com";
        final String password = "password";
        final String encryptedPassword = "encryptedPassword";
        final boolean admin = false;
        final Role roleMock = Mockito.mock(Role.class);
        final User userMock = Mockito.mock(User.class);
        Mockito.when(userRepository.existsByUsername(Mockito.any())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(Mockito.any())).thenReturn(false);
        Mockito.when(roleRepository.findByName(Mockito.any())).thenReturn(Optional.of(roleMock));
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encryptedPassword);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userMock);
        final ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userService.registerUser(firstName, lastName, middleName, username, mail, password, admin);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue())
                .matches(user -> firstName.equals(user.getFirstName())
                                 && lastName.equals(user.getLastName())
                                 && middleName.equals(user.getMiddleName())
                                 && username.equals(user.getUsername())
                                 && mail.equals(user.getEmail())
                                 && encryptedPassword.equals(user.getPassword()));
    }

    @Test
    void editUser() {
        final String firstName = "firstName";
        final String lastName = "lastName";
        final String middleName = "middleName";
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        final ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userService.updateUserData(42L, firstName, lastName, middleName);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue())
                .matches(user -> firstName.equals(user.getFirstName())
                                 && lastName.equals(user.getLastName())
                                 && middleName.equals(user.getMiddleName()));
    }
}