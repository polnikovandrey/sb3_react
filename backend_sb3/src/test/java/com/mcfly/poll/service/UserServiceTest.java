package com.mcfly.poll.service;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PollRepository pollRepository;
    @Mock
    VoteRepository voteRepository;
    @InjectMocks
    UserService userService;

    @Test
    void contextLoads() {
        assertThat(userService).isNotNull();
    }

    @Test
    void userSummary() {
        final UserService userServiceMock = Mockito.mock();
        final UserSummary userSummaryToReturn = new UserSummary(1L, "username", "firstName", "lastName", "middleName");
        Mockito.when(userServiceMock.getUserSummary(Mockito.any(UserPrincipal.class)))
                .thenReturn(userSummaryToReturn);
        final UserSummary userSummary = userServiceMock.getUserSummary(new UserPrincipal());
        assertThat(userSummary).usingRecursiveComparison().isEqualTo(userSummaryToReturn);
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
        final String absentUsername = "Absent user";
        Mockito.when(userRepository.findByUsername(absentUsername))
                .thenThrow(new ResourceNotFoundException("User not found", "User", "username", absentUsername));
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserProfile(absentUsername));
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.any());
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoInteractions(pollRepository);
        Mockito.verifyNoInteractions(voteRepository);
    }

    @Test
    void getExistingUserProfile() {
        final String existingUsername = "Existing user";
        final User expectedUser =
                User.builder()
                        .id(1L)
                        .username(existingUsername)
                        .email("user@email.com")
                        .password("password")
                        .firstName("firstName")
                        .lastName("lastName")
                        .middleName("middleName")
                        .build();
        final PollingUserProfile expectedProfile
                = new PollingUserProfile(expectedUser.getId(), expectedUser.getUsername(), expectedUser.getLastName(),
                expectedUser.getCreatedAt(), 42L, 43L);
        Mockito.when(userRepository.findByUsername(existingUsername))
                .thenReturn(Optional.of(expectedUser));
        Mockito.when(pollRepository.countByCreatedBy(expectedUser.getId()))
                        .thenReturn(42L);
        Mockito.when(voteRepository.countByUserId(expectedUser.getId()))
                .thenReturn(43L);
        final PollingUserProfile actual = userService.getUserProfile(existingUsername);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedProfile);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.any());
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verify(pollRepository, Mockito.times(1)).countByCreatedBy(Mockito.any());
        Mockito.verifyNoMoreInteractions(pollRepository);
        Mockito.verify(voteRepository, Mockito.times(1)).countByUserId(Mockito.any());
        Mockito.verifyNoMoreInteractions(voteRepository);
    }

    @Test
    void listUsersPage() {
        final Pageable pageable = PageRequest.of(0, 1, Sort.Direction.ASC, "id");
        final List<User> users = List.of(User.builder().id(42L).username("username").roles(Set.of()).build());
        final List<UserResponse> expectedUserResponses
                = users.stream().map(user -> UserResponse.builder().id(user.getId()).username(user.getUsername())
                                                         .email(user.getEmail()).firstName(user.getFirstName())
                                                         .lastName(user.getLastName()).middleName(user.getMiddleName())
                                                         .roles(Set.of())
                                                         .build()).toList();
        final PagedResponse<UserResponse> expectedPagedResponse = new PagedResponse<>(expectedUserResponses, 0, 1, 1, 1, true);
        final PageImpl<User> page = new PageImpl<>(users, pageable, 1);
        Mockito.when(userRepository.findAll(pageable))
               .thenReturn(page);
        final PagedResponse<UserResponse> userResponsePagedResponse = userService.listUsersPage(0, 1);
        assertThat(userResponsePagedResponse).usingRecursiveComparison().isEqualTo(expectedPagedResponse);
        Mockito.verify(userRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
    }

    @Test
    void getLastPage() {
        Mockito.when(userRepository.count()).thenReturn(0L);
        assertThat(userService.getLastPageIndex(1)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(2)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(5)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(10)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(20)).isEqualTo(0);
        Mockito.when(userRepository.count()).thenReturn(1L);
        assertThat(userService.getLastPageIndex(1)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(2)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(5)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(10)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(20)).isEqualTo(0);
        Mockito.when(userRepository.count()).thenReturn(5L);
        assertThat(userService.getLastPageIndex(1)).isEqualTo(4);
        assertThat(userService.getLastPageIndex(2)).isEqualTo(2);
        assertThat(userService.getLastPageIndex(5)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(10)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(20)).isEqualTo(0);
        Mockito.when(userRepository.count()).thenReturn(10L);
        assertThat(userService.getLastPageIndex(1)).isEqualTo(9);
        assertThat(userService.getLastPageIndex(2)).isEqualTo(4);
        assertThat(userService.getLastPageIndex(5)).isEqualTo(1);
        assertThat(userService.getLastPageIndex(10)).isEqualTo(0);
        assertThat(userService.getLastPageIndex(20)).isEqualTo(0);
        Mockito.when(userRepository.count()).thenReturn(11L);
        assertThat(userService.getLastPageIndex(1)).isEqualTo(10);
        assertThat(userService.getLastPageIndex(2)).isEqualTo(5);
        assertThat(userService.getLastPageIndex(5)).isEqualTo(2);
        assertThat(userService.getLastPageIndex(10)).isEqualTo(1);
        assertThat(userService.getLastPageIndex(20)).isEqualTo(0);
        Mockito.when(userRepository.count()).thenReturn(19L);
        assertThat(userService.getLastPageIndex(1)).isEqualTo(18);
        assertThat(userService.getLastPageIndex(2)).isEqualTo(9);
        assertThat(userService.getLastPageIndex(5)).isEqualTo(3);
        assertThat(userService.getLastPageIndex(10)).isEqualTo(1);
        assertThat(userService.getLastPageIndex(20)).isEqualTo(0);
    }

    /* TODO
        com.mcfly.poll.service.UserService.registerUser
        com.mcfly.poll.service.UserService.deleteUserById
        com.mcfly.poll.service.UserService.findUserById
        com.mcfly.poll.service.UserService.editUser
     */
}