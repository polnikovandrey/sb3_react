package com.mcfly.template.exception;

public class UserExistsAlreadyException extends RuntimeException {

    public UserExistsAlreadyException(String message) {
        super(message);
    }
}
