package com.DriveAway.project.controller;

import com.DriveAway.project.dto.*;
import com.DriveAway.project.exception.UserNotFoundException;
import com.DriveAway.project.model.User;
import com.DriveAway.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO getSampleUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("john@example.com");
        userDTO.setUsername("John");
        userDTO.setPassword("Password@123");
        userDTO.setAadharNumber("234567891234");
        userDTO.setDrivingLicense("DL1234567890123");
        userDTO.setMobileNumber("9876543210");
        userDTO.setAltMobileNumber("9123456780");
        userDTO.setRole("USER");
        userDTO.setStatus("PENDING");

        AddressDTO address = new AddressDTO();
        address.setStreet("123 Street");
        address.setCity("City");
        address.setState("State");
        address.setPostalCode("123456");
        address.setCountry("India");

        userDTO.setAddress(address);
        return userDTO;
    }

    private UserResponseDTO getSampleUserResponseDTO() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setEmail("john@example.com");
        dto.setUsername("John");
        dto.setAadharNumber("234567891234");
        dto.setDrivingLicense("DL1234567890123");
        dto.setMobileNumber("9876543210");
        dto.setAltMobileNumber("9123456780");

        UserResponseDTO.AddressDTO nestedAddress = new UserResponseDTO.AddressDTO();
        nestedAddress.setStreet("123 Street");
        nestedAddress.setCity("City");
        nestedAddress.setState("State");
        nestedAddress.setPostalCode("123456");
        nestedAddress.setCountry("India");

        dto.setAddress(nestedAddress);
        return dto;
    }

    @Test
    @DisplayName("Register User - Success")
    void testRegisterUser() throws Exception {
        UserDTO userDTO = getSampleUserDTO();
        User savedUser = new User();
        savedUser.setUserId(1L);
        savedUser.setEmail(userDTO.getEmail());
        savedUser.setUsername(userDTO.getUsername());

        when(userService.registerUser(any(UserDTO.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("Register User - Validation Failure")
    void testRegisterUser_InvalidInput() throws Exception {
        UserDTO invalidUser = new UserDTO(); // empty userDTO

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get User By ID")
    void testGetUserById() throws Exception {
        UserResponseDTO responseDTO = getSampleUserResponseDTO();
        when(userService.getUserById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John"));
    }

    @Test
    @DisplayName("Get User By Email")
    void testGetUserByEmail() throws Exception {
        UserResponseDTO responseDTO = getSampleUserResponseDTO();
        when(userService.getUserByEmail("john@example.com")).thenReturn(responseDTO);

        mockMvc.perform(get("/users/check-email/john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("Get All Users")
    void testGetAllUsers() throws Exception {
        UserDTO userDTO = getSampleUserDTO();
        when(userService.getAllUsers()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    @DisplayName("Update User")
    void testUpdateUser() throws Exception {
        UserResponseDTO requestDTO = getSampleUserResponseDTO();
        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setUsername("John");

        when(userService.updateUser(eq(1L), any(UserResponseDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John"));
    }

    @Test
    @DisplayName("Delete User")
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    @DisplayName("Login Success")
    void testLoginSuccess() throws Exception {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("john@example.com");
        loginDTO.setPassword("Password@123");

        AuthResponseDTO authResponse = new AuthResponseDTO(1L, "John", "USER","john@example.com");

        when(userService.authenticateUser(anyString(), anyString())).thenReturn(authResponse);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userName").value("John"))
                .andExpect(jsonPath("$.role").value("USER"))
        		.andExpect(jsonPath("$.email").value("john@example.com"));
        
    }

    @Test
    @DisplayName("Login - User Not Found")
    void testLoginUserNotFound() throws Exception {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("notfound@example.com");
        loginDTO.setPassword("pass");

        when(userService.authenticateUser(anyString(), anyString()))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    @DisplayName("Get Pending Users")
    void testGetPendingUsers() throws Exception {
        UserDTO userDTO = getSampleUserDTO();
        when(userService.getNewUsers()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/users/pendingUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    @DisplayName("Update User Status - Success")
    void testUpdateUserStatus() throws Exception {
        UpdateUserStatusDTO statusDTO = new UpdateUserStatusDTO();
        statusDTO.setStatus("ACCEPTED");

        doNothing().when(userService).updateUserStatus(eq(1L), eq("ACCEPTED"));

        mockMvc.perform(put("/users/updateUserStatus/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("User status updated successfully"));
    }

    @Test
    @DisplayName("Update User Status - User Not Found")
    void testUpdateUserStatus_UserNotFound() throws Exception {
        UpdateUserStatusDTO statusDTO = new UpdateUserStatusDTO();
        statusDTO.setStatus("REJECTED");

        doThrow(new UserNotFoundException("User not found"))
                .when(userService).updateUserStatus(eq(99L), eq("REJECTED"));

        mockMvc.perform(put("/users/updateUserStatus/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    @DisplayName("Forgot Password")
    void testForgotPassword() throws Exception {
        ForgotPasswordDTO forgotPasswordDTO = new ForgotPasswordDTO();
        forgotPasswordDTO.setEmail("john@example.com");
        forgotPasswordDTO.setNewPassword("NewPass@123");

        doNothing().when(userService).resetPassword(any(ForgotPasswordDTO.class));

        mockMvc.perform(post("/users/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(forgotPasswordDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful."));
    }
}
