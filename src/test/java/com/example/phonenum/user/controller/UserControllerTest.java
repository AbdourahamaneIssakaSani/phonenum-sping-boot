package com.example.phonenum.user.controller;

import com.example.phonenum.user.model.User;
import com.example.phonenum.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.javafaker.Faker;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
//        User user = new User("John Doe", "+250791431951", "johnpwd");
//        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void canCreateNewUser() throws Exception {
        User user = new User("Jane Doe", "+250791431950", "janepwd");

        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)));

        resultActions.andExpect(status().isCreated());
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        User newUser = objectMapper.readValue(responseContent, User.class);
        assertNotNull(newUser.getId());
        assertEquals(user.getName(), newUser.getName());
        assertEquals(user.getPhoneNumber(), newUser.getPhoneNumber());
        assertEquals(user.getPassword(), newUser.getPassword());
    }

    @Test
    void canGetAllUsers() throws Exception {
        User user1 = new User("John Doe", "+250791431951", "johnpwd");
        User user2 = new User("Jane Doe", "+250791431952", "janepwd");
        userRepository.save(user1);
        userRepository.save(user2);
        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/v1/users"));
        // then
        resultActions.andExpect(status().isOk());
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        List<User> users = objectMapper.readValue(responseContent, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
        assertEquals(2, users.size());
    }

    @Test
    void canGetUserById() throws Exception {
        User user = new User("John Doe", "+250791431951", "johnpwd");
        User savedUser = userRepository.save(user);
        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/v1/users/{id}", savedUser.getId()));
        // then
        resultActions.andExpect(status().isOk());
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        User returnedUser = objectMapper.readValue(responseContent, User.class);
        assertNotNull(returnedUser);
        assertEquals(savedUser.getName(), returnedUser.getName());
        assertEquals(savedUser.getPhoneNumber(), returnedUser.getPhoneNumber());
        assertEquals(savedUser.getPassword(), returnedUser.getPassword());
    }

    @Test
    void canGetUserByPhoneNumber() throws Exception {
        User user = new User("John Doe", "+250791431950", "johnpwd");
        userRepository.save(user);

        mockMvc.perform(get("/api/v1/users")
                        .param("phoneNumber", user.getPhoneNumber().substring(1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));
    }
}