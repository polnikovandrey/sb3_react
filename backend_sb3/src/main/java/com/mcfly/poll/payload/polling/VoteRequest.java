package com.mcfly.poll.payload.polling;

import jakarta.validation.constraints.NotNull;

public class VoteRequest {

    @NotNull
    private Long choiceId;

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }
}
