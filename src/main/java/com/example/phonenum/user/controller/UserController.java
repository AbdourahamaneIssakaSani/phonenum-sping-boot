package com.example.phonenum.user.controller;

import com.example.phonenum.user.exception.UserNotFoundException;
import com.example.phonenum.user.model.User;
import com.example.phonenum.user.service.UserService;
import com.google.i18n.phonenumbers.NumberParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) throws NumberParseException {

        User newUser = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) throws UserNotFoundException {
        Optional<User> user = userService.getUser(id);
        return user.map(ResponseEntity::ok).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping(params = "phoneNumber")
    public ResponseEntity<User> getUserByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) throws UserNotFoundException {
        // add the plus sign to the phone number in the query
        phoneNumber = "+" + phoneNumber;
        Optional<User> user = userService.getUser(phoneNumber);
        return user.map(ResponseEntity::ok).orElseThrow(UserNotFoundException::new);
    }
}
