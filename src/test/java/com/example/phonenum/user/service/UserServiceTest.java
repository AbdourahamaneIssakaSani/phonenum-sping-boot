package com.example.phonenum.user.service;

import com.example.phonenum.user.exception.UserAlreadyExistsException;
import com.example.phonenum.user.model.User;
import com.example.phonenum.user.repository.UserRepository;
import com.google.i18n.phonenumbers.NumberParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private AutoCloseable autoCloseable;
    private UserService userService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllUsers() {
        userService.getUsers();
        verify(userRepository).findAll();
    }

    @Test
    void canGetAUser() {
        userService.getUser(1);
        verify(userRepository).findById(1);
    }

    @Test
    void canCreateUser() throws NumberParseException {

        User user = new User("John Doe", "+250791431950", "johnpwd");

        userService.createUser(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void willThrowWhenInvalidNumberFormat() throws NumberFormatException {
        User user = new User("John Doe", "+1234", "johnpwd");

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(NumberFormatException.class)
                .hasMessage("Invalid phone number");

        verify(userRepository, never()).save(user);
    }

    @Test
    void willThrowWhenInvalidPhoneNumber() {
        User user = new User("John Doe", "+10001234567", "johnpwd");

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(NumberFormatException.class)
                .hasMessage("Invalid phone number");

        verify(userRepository, never()).save(user);
    }

    @Test
    void canCreateNewUser() throws NumberFormatException, NumberParseException {
        User user = new User("John Doe", "+14155552671", "johnpwd");
        when(userRepository.findByPhoneNumber(user.getPhoneNumber()))
                .thenReturn(Optional.empty());
        when(userRepository.save(user))
                .thenReturn(user);

        User newUser = userService.createUser(user);

        assertThat(newUser).isEqualTo(user);

        verify(userRepository).save(user);
    }

    @Test
    void willThrowWhenUserAlreadyExists() {
        User user = new User("John Doe", "+14155552671", "johnpwd");
        when(userRepository.findByPhoneNumber(user.getPhoneNumber()))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("A user with this phone number already exists!");

        verify(userRepository, never()).save(user);
    }

/*   @Test
    void itShouldCheckForExistingPhoneNumber() throws NumberParseException {

        User user = new User("John Doe", "+250791431950", "johnpwd");
        when(userService.createUser(user)).thenReturn(user);

        User newUser = userService.createUser(user);

        verify(userRepository).save(user);

        // given(userService.isExistingPhoneNumber(newUser.getPhoneNumber())).willReturn(true);

        boolean isExisting = userService.isExistingPhoneNumber(newUser.getPhoneNumber());

        assertThat(isExisting).isTrue();

    }
 */

    @Test
    void itShouldCheckForNonExistingPhoneNumber() {

        User user = new User("John Doe", "+250791431950", "johnpwd");
        userRepository.save(user);

        boolean notExisting = userService.isExistingPhoneNumber("+987654321");
        assertFalse(notExisting);
    }
}