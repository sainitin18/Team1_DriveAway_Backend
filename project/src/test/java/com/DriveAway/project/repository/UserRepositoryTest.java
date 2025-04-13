package com.DriveAway.project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.DriveAway.project.model.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        user.setPassword("securePassword123");
        user.setAadharNumber("123456789012");
        user.setDrivingLicense("DL1234567890");
        user.setMobileNumber("9876543220");
        user.setAltMobileNumber("9123456780");
        user.setRole("USER");
        user.setStatus("ACTIVE");
    }

    @AfterEach
    public void tearDown() {
        user = null;
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("JUnit test for save User operation")
    public void givenUserObject_whenSave_thenReturnSavedUser() {
        User savedUser = userRepository.save(user);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("JUnit test for find all Users operation")
    public void givenUsersList_whenFindAll_thenReturnUsersList() {
        User user1 = new User();
        user1.setEmail("alice@example.com");
        user1.setUsername("alice");
        user1.setPassword("password123");
        user1.setAadharNumber("111122223333");
        user1.setDrivingLicense("DL000111222");
        user1.setMobileNumber("9876512345");
        user1.setAltMobileNumber("9123456789");
        user1.setRole("USER");
        user1.setStatus("ACTIVE");

        User user2 = new User();
        user2.setEmail("bob@example.com");
        user2.setUsername("bob");
        user2.setPassword("password456");
        user2.setAadharNumber("444455556666");
        user2.setDrivingLicense("DL333444555");
        user2.setMobileNumber("9876523456");
        user2.setAltMobileNumber("9876543210");
        user2.setRole("ADMIN");
        user2.setStatus("INACTIVE");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(13);
    }

    @Test
    @DisplayName("JUnit test for find User by Email operation")
    public void givenUser_whenFindByEmail_thenReturnUser() {
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("JUnit test for find Users by Status operation")
    public void givenStatus_whenFindByStatus_thenReturnUsersList() {
        userRepository.save(user);
        List<User> activeUsers = userRepository.findByStatus("ACTIVE");
        assertThat(activeUsers).isNotEmpty();
        assertThat(activeUsers.get(0).getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("JUnit test for update User operation")
    public void givenUser_whenUpdate_thenReturnUpdatedUser() {
        userRepository.save(user);
        String newMobile = "9998887776";
        User foundUser = userRepository.findById(user.getUserId()).get();
        foundUser.setMobileNumber(newMobile);
        User updatedUser = userRepository.save(foundUser);
        assertThat(updatedUser.getMobileNumber()).isEqualTo(newMobile);
    }

    @Test
    @DisplayName("JUnit test for delete User by Id operation")
    public void givenUser_whenDeleteById_thenRemoveUser() {
        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getUserId());
        Optional<User> optionalUser = userRepository.findById(savedUser.getUserId());
        assertThat(optionalUser).isEmpty();
    }
}
