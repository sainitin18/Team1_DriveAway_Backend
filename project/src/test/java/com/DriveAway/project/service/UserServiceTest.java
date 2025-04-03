package com.DriveAway.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.DriveAway.project.dto.UserDTO;
import com.DriveAway.project.exception.UserAlreadyExistsException;
import com.DriveAway.project.exception.UserNotFoundException;
import com.DriveAway.project.model.User;
import com.DriveAway.project.repository.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user = new User();
        user.setUserId(1L);
        user.setEmail("testuser@example.com");
        user.setUsername("TestUser");
        user.setPassword("Test@123");
        user.setAadharNumber("234567890123");
        user.setDrivingLicense("AB1234567890123");
        user.setMobileNumber("9876543210");
        user.setAltMobileNumber("8765432109");
        user.setRole("USER");
        user.setStatus("Pending");
        
        userDTO = new UserDTO();
        userDTO.setEmail("testuser@example.com");
        userDTO.setUsername("TestUser");
        userDTO.setPassword("Test@123");
        userDTO.setAadharNumber("234567890123");
        userDTO.setDrivingLicense("AB1234567890123");
        userDTO.setMobileNumber("9876543210");
        userDTO.setAltMobileNumber("8765432109");
        userDTO.setRole("USER");
        userDTO.setStatus("Pending");
    }

    // ✅ Test for registering a user
    @Test
    @DisplayName("JUnit test for registering a user")
    public void givenUserDTO_whenRegisterUser_thenReturnSavedUser() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registerUser(userDTO);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(userDTO.getEmail());
    }

    // ❌ Test for registering a duplicate user
    @Test
    @DisplayName("JUnit test for registering a user with existing email")
    public void givenExistingEmail_whenRegisterUser_thenThrowException() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userDTO));
    }

    // ✅ Test for fetching all users
    @Test
    @DisplayName("JUnit test for getting all users")
    public void whenGetAllUsers_thenReturnUserList() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.getAllUsers();

        assertThat(retrievedUsers).hasSize(1);
    }

    // ✅ Test for getting a user by ID
    @Test
    @DisplayName("JUnit test for finding a user by ID")
    public void givenUserId_whenGetUserById_thenReturnUser() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(user.getUserId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(user.getUserId());
    }

    // ❌ Test for getting a non-existing user by ID
    @Test
    @DisplayName("JUnit test for finding a non-existing user by ID")
    public void givenInvalidUserId_whenGetUserById_thenThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L));
    }

    // ✅ Test for updating a user
    @Test
    @DisplayName("JUnit test for updating a user")
    public void givenUserDTO_whenUpdateUser_thenReturnUpdatedUser() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(user.getUserId(), userDTO);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo(userDTO.getUsername());
    }

    // ✅ Test for deleting a user
    @Test
    @DisplayName("JUnit test for deleting a user")
    public void givenUserId_whenDeleteUser_thenRemoveUser() {
        when(userRepository.existsById(user.getUserId())).thenReturn(true);
        doNothing().when(userRepository).deleteById(user.getUserId());

        userService.deleteUser(user.getUserId());

        verify(userRepository, times(1)).deleteById(user.getUserId());
    }

    // ❌ Test for deleting a non-existing user
    @Test
    @DisplayName("JUnit test for deleting a non-existing user")
    public void givenInvalidUserId_whenDeleteUser_thenThrowException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(99L));
    }
}
