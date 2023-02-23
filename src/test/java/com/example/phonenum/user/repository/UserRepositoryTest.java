package com.example.phonenum.user.repository;

import com.example.phonenum.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(new User("John Doe", "+250791431950", "johnpwd"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void itShouldCheckIfUserIsPresent() {
        Optional<User> user = userRepository.findByPhoneNumber("+250791431950");
        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("John Doe");
        assertThat(user.get().getPassword()).isEqualTo("johnpwd");
    }

    @Test
    void itShouldCheckIfNonExistingNumberReturnsEmpty() {
        Optional<User> user = userRepository.findByPhoneNumber("+9876543210");
        assertThat(user).isEmpty();
    }
}