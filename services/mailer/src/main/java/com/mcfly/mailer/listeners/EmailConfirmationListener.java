package com.mcfly.mailer.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcfly.mailer.payload.queue.EmailConfirmationPayload;
import org.springframework.stereotype.Component;

@Component
public class EmailConfirmationListener {

    @SuppressWarnings("unused")
    public void handleMessage(String message) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final TypeReference<EmailConfirmationPayload> mapType = new TypeReference<>() {};
        final EmailConfirmationPayload payload = objectMapper.readValue(message, mapType);
        System.out.printf("Message received:%n%s%n", payload);
    }
}
