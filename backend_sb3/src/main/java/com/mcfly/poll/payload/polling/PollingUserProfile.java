package com.mcfly.poll.payload.polling;

import com.mcfly.poll.payload.user_role.UserProfile;

import java.time.Instant;

public class PollingUserProfile extends UserProfile {

    private Long pollCount;
    private Long voteCount;

    public PollingUserProfile() {
    }

    public PollingUserProfile(Long id, String username, String name, Instant joinedAt, Long pollCount, Long voteCount) {
        super(id, username, name, joinedAt);
        this.pollCount = pollCount;
        this.voteCount = voteCount;
    }

    public Long getPollCount() {
        return pollCount;
    }

    public void setPollCount(Long pollCount) {
        this.pollCount = pollCount;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }
}
