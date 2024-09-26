package com.wales.book_socials.exception;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String message) {
        super(message);
    }
}
