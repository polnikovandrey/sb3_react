package com.mcfly.mailer.payload.queue;

public record EmailConfirmationPayload(long userId, String email, int confirmationCode) {

    @Override
    public String toString() {
        return "EmailConfirmationPayload{" +
               "userId=" + userId +
               ", email='" + email + '\'' +
               ", confirmationCode=" + confirmationCode +
               '}';
    }
}