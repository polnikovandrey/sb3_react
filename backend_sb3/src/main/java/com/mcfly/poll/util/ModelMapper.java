package com.mcfly.poll.util;

import com.mcfly.poll.domain.polling.Poll;
import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.payload.polling.ChoiceResponse;
import com.mcfly.poll.payload.polling.PollResponse;
import com.mcfly.poll.payload.user_role.UserSummary;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ModelMapper {

    public static PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator, Long userVote) {
        final PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        final Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));
        final List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            final ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());
            if (choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
            } else {
                choiceResponse.setVoteCount(0);
            }
            return choiceResponse;
        }).toList();
        pollResponse.setChoices(choiceResponses);
        final UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getFirstName(), creator.getLastName(), creator.getMiddleName());
        pollResponse.setCreatedBy(creatorSummary);
        if (userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }
        final long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);
        return pollResponse;
    }
}
