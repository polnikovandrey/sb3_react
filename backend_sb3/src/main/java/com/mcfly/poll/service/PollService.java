package com.mcfly.poll.service;

import com.mcfly.poll.domain.polling.Choice;
import com.mcfly.poll.domain.polling.ChoiceVoteCount;
import com.mcfly.poll.domain.polling.Poll;
import com.mcfly.poll.domain.polling.Vote;
import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.exception.BadRequestException;
import com.mcfly.poll.exception.ResourceNotFoundException;
import com.mcfly.poll.payload.polling.PagedResponse;
import com.mcfly.poll.payload.polling.PollRequest;
import com.mcfly.poll.payload.polling.PollResponse;
import com.mcfly.poll.payload.polling.VoteRequest;
import com.mcfly.poll.repository.polling.PollRepository;
import com.mcfly.poll.repository.polling.VoteRepository;
import com.mcfly.poll.repository.user_role.UserRepository;
import com.mcfly.poll.security.UserPrincipal;
import com.mcfly.poll.util.AppConstants;
import com.mcfly.poll.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PollService {

    private static final Logger logger = LoggerFactory.getLogger(PollService.class);

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    public PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);
        final Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        final Page<Poll> polls = pollRepository.findAll(pageable);
        if (polls.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), polls.getNumber(), polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
        }
        final List<Long> pollIds = polls.map(Poll::getId).getContent();
        final Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
        final Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
        final Map<Long, User> creatorMap = getPollCreatorMap(polls.getContent());
        final List<PollResponse> pollResponses
                = polls.map(poll -> ModelMapper
                .mapPollToPollResponse(
                        poll,
                        choiceVoteCountMap,
                        creatorMap.get(poll.getCreatedBy()),
                        pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null)))
                .getContent();
        return new PagedResponse<>(pollResponses, polls.getNumber(), polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
    }

    public PagedResponse<PollResponse> getPollsCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        final User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found", "User", "username", username));
        final Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        final Page<Poll> polls = pollRepository.findByCreatedBy(user.getId(), pageable);
        if (polls.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), polls.getNumber(), polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
        }
        List<Long> pollIds = polls.map(Poll::getId).getContent();
        final Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
        final Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
        final List<PollResponse> pollResponses
                = polls.map(poll -> ModelMapper
                .mapPollToPollResponse(
                        poll,
                        choiceVoteCountMap,
                        user,
                        pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null)))
                .getContent();
        return new PagedResponse<>(pollResponses, polls.getNumber(), polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
    }

    public PagedResponse<PollResponse> getPollsVotedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);
        final User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found", "User", "username", username));
        final Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        final Page<Long> userVotedPollIds = voteRepository.findVotedPollIdsByUserId(user.getId(), pageable);
        if (userVotedPollIds.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), userVotedPollIds.getNumber(), userVotedPollIds.getSize(), userVotedPollIds.getTotalElements(), userVotedPollIds.getTotalPages(), userVotedPollIds.isLast());
        }
        final List<Long> pollIds = userVotedPollIds.getContent();
        final Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        final List<Poll> polls = pollRepository.findByIdIn(pollIds, sort);
        final Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
        final Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
        final Map<Long, User> creatorMap = getPollCreatorMap(polls);
        final List<PollResponse> pollResponses = polls.stream().map(poll ->
                ModelMapper
                        .mapPollToPollResponse(
                                poll,
                                choiceVoteCountMap,
                                creatorMap.get(poll.getCreatedBy()),
                                pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null)
                        )).toList();
        return new PagedResponse<>(pollResponses, userVotedPollIds.getNumber(), userVotedPollIds.getSize(), userVotedPollIds.getTotalElements(), userVotedPollIds.getTotalPages(), userVotedPollIds.isLast());
    }

    public Poll createPoll(PollRequest pollRequest) {
        final Poll poll = new Poll();
        poll.setQuestion(pollRequest.getQuestion());
        pollRequest.getChoices().forEach(choiceRequest -> poll.addChoice(new Choice(choiceRequest.getText())));
        final Instant now = Instant.now();
        final Instant expirationDateTime = now.plus(Duration.ofDays(pollRequest.getPollLength().getDays())).plus(Duration.ofHours(pollRequest.getPollLength().getHours()));
        poll.setExpirationDateTime(expirationDateTime);
        return pollRepository.save(poll);
    }

    public PollResponse getPollById(Long pollId, UserPrincipal currentUser) {
        final Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResourceNotFoundException("Poll not found", "Poll", "id", pollId));
        final List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);
        final Map<Long, Long> choiceVotesMap = votes.stream().collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));
        final User creator = userRepository.findById(poll.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User not found", "User", "id", poll.getCreatedBy()));
        Vote userVote = null;
        if (currentUser != null) {
            userVote = voteRepository.findByUserIdAndPollId(currentUser.getId(), pollId);
        }
        return ModelMapper.mapPollToPollResponse(poll, choiceVotesMap, creator, userVote == null ? null : userVote.getChoice().getId());
    }

    public PollResponse castVoteAndGetUpdatedPoll(Long pollId, VoteRequest voteRequest, UserPrincipal currentUser) {
        final Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResourceNotFoundException("Poll not found", "Poll", "id", pollId));
        if (poll.getExpirationDateTime().isBefore(Instant.now())) {
            throw new BadRequestException("Poll expired!");
        }
        final User user = userRepository.getOne(currentUser.getId());
        final Choice selectedChoice = poll.getChoices().stream()
                .filter(choice -> choice.getId().equals(voteRequest.getChoiceId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Choice not found", "Choice", "id", voteRequest.getChoiceId()));
        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setUser(user);
        vote.setChoice(selectedChoice);
        try {
            vote = voteRepository.save(vote);
        } catch (DataIntegrityViolationException ex) {
            logger.info("User {} has already voted in Poll {}", currentUser.getId(), pollId);
            throw new BadRequestException("Sorry! You have already cast your vote in this pool.");
        }
        final List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);
        final Map<Long, Long> choiceVotesMap = votes.stream().collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));
        final User creator = userRepository.findById(poll.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User not found", "User", "id", poll.getCreatedBy()));
        return ModelMapper.mapPollToPollResponse(poll, choiceVotesMap, creator, vote.getChoice().getId());
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    private Map<Long, Long> getChoiceVoteCountMap(List<Long> pollIds) {
        final List<ChoiceVoteCount> votes = voteRepository.countByPollIdInGroupByChoiceId(pollIds);
        return votes.stream().collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));
    }

    private Map<Long, Long> getPollUserVoteMap(UserPrincipal currentUser, List<Long> pollIds) {
        Map<Long, Long> pollUserVoteMap = null;
        if (currentUser != null) {
            final List<Vote> userVotes = voteRepository.findByUserIdAndPollIdIn(currentUser.getId(), pollIds);
            pollUserVoteMap = userVotes.stream().collect(Collectors.toMap(vote -> vote.getPoll().getId(), vote -> vote.getChoice().getId()));
        }
        return pollUserVoteMap;
    }

    Map<Long, User> getPollCreatorMap(List<Poll> polls) {
        final List<Long> creatorIds = polls.stream().map(Poll::getCreatedBy).distinct().toList();
        final List<User> creators = userRepository.findByIdIn(creatorIds);
        return creators.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }
}
