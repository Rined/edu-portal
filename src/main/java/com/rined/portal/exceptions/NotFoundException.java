package com.rined.portal.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String template, Object... objs) {
        super(String.format(template, objs));
    }
}
