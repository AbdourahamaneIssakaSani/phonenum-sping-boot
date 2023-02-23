package com.example.phonenum.user.exception;

import com.google.i18n.phonenumbers.NumberParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller advice to handle exceptions related to user operations.
 */
@ControllerAdvice
public class UserExceptionHandler {

    /**
     * Handles {@link NumberParseException} and returns a Bad Request response.
     *
     * @param ex The NumberParseException to handle.
     * @return The {@link ResponseEntity} with the Bad Request status code and the exception message.
     */
    @ExceptionHandler(NumberParseException.class)
    public ResponseEntity<String> handleNumberParseException(NumberParseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    /**
     * Handles {@link UserAlreadyExistsException} and returns a Conflict response.
     *
     * @param ex The UserAlreadyExistsException to handle.
     * @return The {@link ResponseEntity} with the Conflict status code and the exception message.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }


    /**
     * Handles {@link UserNotFoundException} and returns a Not Found response.
     *
     * @param ex The UserNotFoundException to handle.
     * @return The {@link ResponseEntity} with the Not Found status code and the exception message.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
