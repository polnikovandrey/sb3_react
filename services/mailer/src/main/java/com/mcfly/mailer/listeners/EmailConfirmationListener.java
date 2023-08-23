package com.mcfly.mailer.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcfly.mailer.payload.queue.EmailConfirmationPayload;
import com.mcfly.mailer.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class EmailConfirmationListener {

    private final MailService mailService;

    public EmailConfirmationListener(MailService mailService) {
        this.mailService = mailService;
    }

    @SuppressWarnings("unused")
    public void handleMessage(String message) throws JsonProcessingException, MessagingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final TypeReference<EmailConfirmationPayload> mapType = new TypeReference<>() {};
        final EmailConfirmationPayload payload = objectMapper.readValue(message, mapType);
        mailService.sendEmailConfirmation(payload.email(), payload.url());
    }
}
