package com.DriveAway.project.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.DriveAway.project.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("testuser@example.com");
        user.setUsername("TestUser");
        user.setPassword("Test@123");
        user.setAadharNumber("234567890123");
        user.setDrivingLicense("AB1234567890123");
        user.setMobileNumber("9876543210");
        user.setAltMobileNumber("8765432109");
        user.setRole("USER");
        user.setStatus("Pending");
    }

    // ✅ Test for saving a user
    @Test
    @DisplayName("JUnit test for saving a user")
    public void givenUserObject_whenSave_thenReturnSavedUser() {
        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isGreaterThan(0);
    }

    // ✅ Test for finding a user by ID
    @Test
    @DisplayName("JUnit test for finding a user by ID")
    public void givenUserId_whenFindById_thenReturnUser() {
        // given
        User savedUser = userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findById(savedUser.getUserId());

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUserId()).isEqualTo(savedUser.getUserId());
    }

    // ✅ Test for finding a user by email
    @Test
    @DisplayName("JUnit test for finding a user by email")
    public void givenEmail_whenFindByEmail_thenReturnUser() {
        // given
        userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    // ✅ Test for deleting a user
    @Test
    @DisplayName("JUnit test for deleting a user")
    public void givenUser_whenDelete_thenRemoveUser() {
        // given
        User savedUser = userRepository.save(user);

        // when
        userRepository.deleteById(savedUser.getUserId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getUserId());

        // then
        assertThat(deletedUser).isEmpty();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}