package com.example.phonenum.user.exception;


/**
 * Thrown when attempting to create a user with a phone number that already exists in the system.
 */
public class UserAlreadyExistsException extends RuntimeException {


    /**
     * Constructs a new {@link UserAlreadyExistsException} with a default message.
     */
    public UserAlreadyExistsException() {
        super("User already exist!");
    }


    /**
     * Constructs a new {@link UserAlreadyExistsException} with the specified message.
     *
     * @param message the detail message.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

