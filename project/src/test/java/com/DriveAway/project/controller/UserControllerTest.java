package com.DriveAway.project.controller;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.DriveAway.project.dto.UserDTO;
import com.DriveAway.project.model.User;
import com.DriveAway.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

class UserControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private UserController userController;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    
    @Test
    void testRegisterUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("TestUser");
        userDTO.setPassword("Password@123");
        userDTO.setAadharNumber("234567890123");
        userDTO.setDrivingLicense("DL1234567890123");
        userDTO.setMobileNumber("9876543210");
        userDTO.setStatus("Active");
        
        User user = new User();
        user.setUserId(1L);
        user.setEmail(userDTO.getEmail());
        
        when(userService.registerUser(any(UserDTO.class))).thenReturn(user);
        
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }
    
    @Test
    void testGetUserById() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");
        
        when(userService.getUserById(1L)).thenReturn(user);
        
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }
    
    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setUserId(1L);
        user1.setEmail("user1@example.com");
        
        User user2 = new User();
        user2.setUserId(2L);
        user2.setEmail("user2@example.com");
        
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);
        
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()));
    }
    
    @Test
    void testUpdateUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("updated@example.com");
        
        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setEmail(userDTO.getEmail());
        
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(updatedUser);
        
        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()));
    }
    
    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);
        
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
    
    @Test
    void testUpdateUserStatus() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setStatus("Active");
        
        doNothing().when(userService).updateUserStatus(1L, "Active");
        
        mockMvc.perform(put("/users/updateUserStatus/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("User status updated successfully"));
    }
}
