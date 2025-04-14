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
    }

    @Test
    @DisplayName("Test for registering a new user")
    public void givenUserDTO_whenRegisterUser_thenReturnSavedUser() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userServiceImpl.registerUser(userDTO);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(userDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test for registering a user with existing email")
    public void givenExistingEmail_whenRegisterUser_thenThrowException() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userServiceImpl.registerUser(userDTO));
    }

    @Test
    @DisplayName("Test for getting all users")
    public void whenGetAllUsers_thenReturnUserList() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userDTO);

        List<UserDTO> retrievedUsers = userServiceImpl.getAllUsers();

        assertThat(retrievedUsers).hasSize(1);
        assertThat(retrievedUsers.get(0).getEmail()).isEqualTo(userDTO.getEmail());
    }

    @Test
    @DisplayName("Test for finding a user by ID")
    public void givenUserId_whenGetUserById_thenReturnUser() {
        // Mock nested AddressDTO
        UserResponseDTO.AddressDTO addressDTO = new UserResponseDTO.AddressDTO();
        addressDTO.setStreet("Old Street");
        addressDTO.setCity("Old City");
        addressDTO.setState("Old State");
        addressDTO.setPostalCode("654321");
        addressDTO.setCountry("Old Country");

        // Set the address and other details in UserResponseDTO
        userResponseDTO.setAddress(addressDTO);
        userResponseDTO.setUsername("TestUser");  // Should match user.getUsername()

        // Mock the repository and mapper
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);

        // Call the service method
        UserResponseDTO result = userServiceImpl.getUserById(user.getUserId());

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getAddress()).isNotNull();
        assertThat(result.getAddress().getCity()).isEqualTo("Old City");
    }



    @Test
    @DisplayName("Test for finding a non-existing user by ID")
    public void givenInvalidUserId_whenGetUserById_thenThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserById(99L));
    }

    @Test
    @DisplayName("Test for updating a user")
    public void givenUserResponseDTO_whenUpdateUser_thenReturnUpdatedUser() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userServiceImpl.updateUser(user.getUserId(), userResponseDTO);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo(userResponseDTO.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test for deleting a user")
    public void givenUserId_whenDeleteUser_thenRemoveUser() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userServiceImpl.deleteUser(user.getUserId());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Test for deleting a non-existing user")
    public void givenInvalidUserId_whenDeleteUser_thenThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.deleteUser(99L));
    }

    @Test
    @DisplayName("Test for authenticating a user")
    public void givenValidEmailAndPassword_whenAuthenticateUser_thenReturnAuthResponse() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), eq(user.getPassword()))).thenReturn(true);

        var authResponse = userServiceImpl.authenticateUser(userDTO.getEmail(), "Test@123");

        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getUserName()).isEqualTo(user.getUsername());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
    }

    @Test
    @DisplayName("Test for authenticating a non-existing user")
    public void givenInvalidEmail_whenAuthenticateUser_thenThrowException() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.authenticateUser(userDTO.getEmail(), "Test@123"));
    }
}
