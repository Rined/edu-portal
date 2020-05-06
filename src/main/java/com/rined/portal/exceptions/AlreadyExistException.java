package com.rined.portal.exceptions;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String template, Object... args) {
        this(String.format(template, args));
    }

    public AlreadyExistException(String message) {
        super(message);
    }
}
