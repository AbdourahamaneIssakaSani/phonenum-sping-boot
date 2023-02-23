package com.example.phonenum.user.service;

import com.example.phonenum.user.exception.UserAlreadyExistsException;
import com.example.phonenum.user.model.User;
import com.example.phonenum.user.repository.UserRepository;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Represents the business logic related to user management.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Instantiate a new user service.
     * Dependency injection is required for UserRepository.
     *
     * @param userRepository The repository to be used to perform CRUD operations on the user entity.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a user by phone number.
     *
     * @param phoneNumber The phone number of the user to be retrieved.
     * @return The user object associated with the given phone number.
     */
    public Optional<User> getUser(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to be retrieved.
     * @return An optional containing the user associated with the given ID if found, or an empty optional if not.
     */
    public Optional<User> getUser(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves all users in the database.
     *
     * @return A list of all users in the database.
     */
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    /**
     * Creates a new user.
     *
     * @param user The user object to be created.
     * @return The newly created user object.
     * @throws NumberFormatException      If the phone number is not in a valid format.
     * @throws NumberParseException       If the phone number cannot be parsed.
     * @throws UserAlreadyExistsException If a user with the same phone number already exists.
     */
    public User createUser(User user) throws NumberFormatException, NumberParseException {
        String phoneNumberString = user.getPhoneNumber();


        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        PhoneNumber phoneNumber;

        try {
            phoneNumber = phoneNumberUtil.parse(phoneNumberString, "");
        } catch (NumberParseException e) {
            throw new NumberParseException(e.getErrorType(), "Invalid/Incomplete phone number");
        }

        if (!phoneNumber.hasCountryCode()) {
            throw new NumberFormatException("Missing country code");
        }

        if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
            throw new NumberFormatException("Invalid phone number");
        }


        if (isExistingPhoneNumber(phoneNumberString)) {
            throw new UserAlreadyExistsException("A user with this phone number already exists!");
        }


        User newUser = userRepository.save(user);

        return newUser;
    }

    /**
     * Checks if a user with the given phone number already exists in the database.
     *
     * @param phoneNumber The phone number to check for.
     * @return True if a user with the given phone number exists in the database, false otherwise.
     */
    protected boolean isExistingPhoneNumber(String phoneNumber) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        return optionalUser.isPresent();
    }
}
