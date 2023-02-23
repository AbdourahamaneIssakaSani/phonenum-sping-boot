package com.example.phonenum.user.exception;

/**
 * Thrown when a requested user is not found given an id.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@link UserNotFoundException } with a default message.
     */
    public UserNotFoundException() {
        super("User not found!");
    }

    /**
     * Constructs a new {@link UserNotFoundException} with the specified message.
     *
     * @param message the message to display when the exception is thrown
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}