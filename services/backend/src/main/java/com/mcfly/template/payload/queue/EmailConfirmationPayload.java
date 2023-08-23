package com.mcfly.template.payload.queue;

public record EmailConfirmationPayload(String email, String url) { }