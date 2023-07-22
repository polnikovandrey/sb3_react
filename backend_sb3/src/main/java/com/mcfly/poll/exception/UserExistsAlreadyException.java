package com.mcfly.poll.exception;

public class UserExistsAlreadyException extends RuntimeException {

    public UserExistsAlreadyException(String message) {
        super(message);
    }
}
