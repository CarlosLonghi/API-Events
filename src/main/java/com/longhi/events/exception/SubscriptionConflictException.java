package com.longhi.events.exception;

public class SubscriptionConflictException extends RuntimeException {
    public SubscriptionConflictException(String message) {
        super(message);
    }
}
