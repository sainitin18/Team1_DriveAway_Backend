package com.DriveAway.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.DriveAway.project.dto.AuthResponseDTO;
import com.DriveAway.project.dto.ForgotPasswordDTO;
import com.DriveAway.project.dto.UserDTO;
import com.DriveAway.project.dto.UserResponseDTO;
import com.DriveAway.project.exception.UserAlreadyExistsException;
import com.DriveAway.project.exception.UserNotFoundException;
import com.DriveAway.project.model.Address;
import com.DriveAway.project.model.User;
import com.DriveAway.project.repository.UserRepository;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private UserDTO userDTO;
    private UserResponseDTO userResponseDTO;
    private ForgotPasswordDTO forgotPasswordDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setEmail("testuser@example.com");
        user.setUsername("TestUser");
        user.setPassword("encryptedPassword");
        user.setAadharNumber("234567890123");
        user.setDrivingLicense("AB1234567890123");
        user.setMobileNumber("9876543210");
        user.setAltMobileNumber("8765432109");
        user.setRole("USER");
        user.setStatus("ACCEPTED");

        Address address = new Address();
        address.setStreet("Old Street");
        address.setCity("Old City");
        address.setState("Old State");
        address.setPostalCode("654321");
        address.setCountry("Old Country");
        address.setUser(user);
        user.setAddress(address);

        userDTO = new UserDTO();
        userDTO.setEmail("testuser@example.com");
        userDTO.setUsername("TestUser");
        userDTO.setPassword("Test@123");
        userDTO.setAadharNumber("234567890123");
        userDTO.setDrivingLicense("AB1234567890123");
        userDTO.setMobileNumber("9876543210");
        userDTO.setAltMobileNumber("8765432109");
        userDTO.setRole("USER");
        userDTO.setStatus("ACCEPTED");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setEmail("testuser@example.com");
        userResponseDTO.setUsername("TestUserUpdated");
        userResponseDTO.setAadharNumber("234567890123");
        userResponseDTO.setDrivingLicense("AB1234567890123");
        userResponseDTO.setMobileNumber("9876543210");
        userResponseDTO.setAltMobileNumber("8765432109");

        UserResponseDTO.AddressDTO addressDTO = new UserResponseDTO.AddressDTO();
        addressDTO.setStreet("New Street");
        addressDTO.setCity("New City");
        addressDTO.setState("New State");
        addressDTO.setPostalCode("123456");
        addressDTO.setCountry("New Country");
        userResponseDTO.setAddress(addressDTO);

        forgotPasswordDTO = new ForgotPasswordDTO();
        forgotPasswordDTO.setEmail("testuser@example.com");
        forgotPasswordDTO.setNewPassword("NewPassword123");
    }

    @Test
    @DisplayName("Register new user - success")
    public void givenUserDTO_whenRegisterUser_thenReturnSavedUser() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userServiceImpl.registerUser(userDTO);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(userDTO.getEmail());
    }

    @Test
    @DisplayName("Register user - email already exists")
    public void givenExistingEmail_whenRegisterUser_thenThrowException() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyExistsException.class, () -> userServiceImpl.registerUser(userDTO));
    }

    @Test
    @DisplayName("Get all users")
    public void whenGetAllUsers_thenReturnUserList() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        List<UserDTO> users = userServiceImpl.getAllUsers();
        assertThat(users).hasSize(1);
    }

    @Test
    @DisplayName("Get user by ID - success")
    public void givenUserId_whenGetUserById_thenReturnUserResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserResponseDTO response = userServiceImpl.getUserById(1L);
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Get user by ID - not found")
    public void givenInvalidId_whenGetUserById_thenThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserById(99L));
    }

    @Test
    @DisplayName("Get user by email - success")
    public void givenEmail_whenGetUserByEmail_thenReturnUserResponseDTO() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserResponseDTO result = userServiceImpl.getUserByEmail(user.getEmail());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Update user - success")
    public void givenUserResponseDTO_whenUpdateUser_thenReturnUpdatedUser() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User updatedUser = userServiceImpl.updateUser(user.getUserId(), userResponseDTO);
        assertThat(updatedUser.getUsername()).isEqualTo(userResponseDTO.getUsername());
    }

    @Test
    @DisplayName("Delete user - success")
    public void givenUserId_whenDeleteUser_thenVerifyDeletion() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        userServiceImpl.deleteUser(user.getUserId());
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Delete user - not found")
    public void givenInvalidUserId_whenDeleteUser_thenThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.deleteUser(99L));
    }

    @Test
    @DisplayName("Authenticate user - success")
    public void givenValidCredentials_whenAuthenticateUser_thenReturnAuthResponseDTO() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), eq(user.getPassword()))).thenReturn(true);
        AuthResponseDTO response = userServiceImpl.authenticateUser(user.getEmail(), "Test@123");
        assertThat(response).isNotNull();
        assertThat(response.getUserName()).isEqualTo(user.getUsername());
    }

    @Test
    @DisplayName("Authenticate user - invalid password")
    public void givenInvalidPassword_whenAuthenticateUser_thenThrowException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), eq(user.getPassword()))).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userServiceImpl.authenticateUser(user.getEmail(), "wrongPassword"));
    }

    @Test
    @DisplayName("Authenticate user - pending status")
    public void givenPendingUser_whenAuthenticateUser_thenThrowException() {
        user.setStatus("PENDING");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertThrows(IllegalStateException.class, () -> userServiceImpl.authenticateUser(user.getEmail(), "Test@123"));
    }

    @Test
    @DisplayName("Authenticate user - rejected status")
    public void givenRejectedUser_whenAuthenticateUser_thenThrowException() {
        user.setStatus("REJECTED");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertThrows(IllegalStateException.class, () -> userServiceImpl.authenticateUser(user.getEmail(), "Test@123"));
    }

    @Test
    @DisplayName("Reset password - accepted user")
    public void givenValidUser_whenResetPassword_thenUpdatePassword() {
        when(userRepository.findByEmail(forgotPasswordDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(forgotPasswordDTO.getNewPassword())).thenReturn("encryptedNewPassword");
        userServiceImpl.resetPassword(forgotPasswordDTO);
        assertThat(user.getPassword()).isEqualTo("encryptedNewPassword");
    }

    @Test
    @DisplayName("Reset password - not accepted user")
    public void givenNonAcceptedUser_whenResetPassword_thenThrowException() {
        user.setStatus("PENDING");
        when(userRepository.findByEmail(forgotPasswordDTO.getEmail())).thenReturn(Optional.of(user));
        assertThrows(IllegalStateException.class, () -> userServiceImpl.resetPassword(forgotPasswordDTO));
    }

    @Test
    @DisplayName("Get new users with status PENDING")
    public void whenGetNewUsers_thenReturnListOfPendingUsers() {
        when(userRepository.findByStatus("PENDING")).thenReturn(List.of(user));
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userDTO);
        List<UserDTO> newUsers = userServiceImpl.getNewUsers();
        assertThat(newUsers).hasSize(1);
    }

    @Test
    @DisplayName("Update user status")
    public void givenUserIdAndStatus_whenUpdateUserStatus_thenVerifyUpdate() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        userServiceImpl.updateUserStatus(user.getUserId(), "REJECTED");
        assertThat(user.getStatus()).isEqualTo("REJECTED");
        verify(userRepository, times(1)).save(user);
    }
}
