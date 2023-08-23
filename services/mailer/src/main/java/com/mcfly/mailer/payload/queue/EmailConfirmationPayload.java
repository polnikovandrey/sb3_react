package com.mcfly.mailer.payload.queue;

public record EmailConfirmationPayload(long userId, String email, String url) {

    @Override
    public String toString() {
        return "EmailConfirmationPayload{" +
               "userId=" + userId +
               ", email='" + email + '\'' +
               ", url='" + url + '\'' +
               '}';
    }
}