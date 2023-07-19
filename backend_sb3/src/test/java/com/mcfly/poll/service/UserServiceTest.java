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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
}
