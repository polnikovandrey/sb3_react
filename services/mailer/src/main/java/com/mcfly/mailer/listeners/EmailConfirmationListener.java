package com.mcfly.mailer.listeners;

import org.springframework.stereotype.Component;

@Component
public class EmailConfirmationListener {

    public void handleMessage(String message) {
        System.out.printf("Received [%s]%n", message);
    }
}
