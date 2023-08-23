package com.mcfly.template.payload.queue;

public record EmailConfirmationPayload(long userId, int confirmationCode) { }
