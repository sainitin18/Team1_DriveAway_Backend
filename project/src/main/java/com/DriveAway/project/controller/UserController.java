package com.DriveAway.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DriveAway.project.dto.AuthResponseDTO;
import com.DriveAway.project.dto.ForgotPasswordDTO;
import com.DriveAway.project.dto.UpdateUserStatusDTO;
import com.DriveAway.project.dto.UserDTO;
import com.DriveAway.project.dto.UserLoginDTO;
import com.DriveAway.project.dto.UserResponseDTO;
import com.DriveAway.project.exception.UserNotFoundException;
import com.DriveAway.project.model.User;
import com.DriveAway.project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
    
    @GetMapping("/check-email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
    	return ResponseEntity.ok(userService.getUserByEmail(email));
    }
    

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody UserResponseDTO userResponseDTO) {
        return ResponseEntity.ok(userService.updateUser(userId, userResponseDTO));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
    
    @PutMapping("updateUserStatus/{userId}")
    public ResponseEntity<String> updateUserStatus(@PathVariable Long userId,@Valid @RequestBody UpdateUserStatusDTO updateUserStatusDTO) {
        userService.updateUserStatus(userId, updateUserStatusDTO.getStatus());
        return ResponseEntity.ok("User status updated successfully");
    }
    
  //  @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
//        boolean isAuthenticated = userService.authenticateUser(userLoginDTO.getEmail(), userLoginDTO.getPassword());
//
//        if (isAuthenticated) {
//            return ResponseEntity.ok("Login successful!");
//        } else {
//            return ResponseEntity.status(401).body("Invalid credentials!");
//        }
//    }
//    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
//        boolean isAuthenticated = userService.authenticateUser(userLoginDTO.getEmail(), userLoginDTO.getPassword());
//
//        if (isAuthenticated) {
//            return ResponseEntity.ok("Login successful");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        try {
            AuthResponseDTO authResponse = userService.authenticateUser(userLoginDTO.getEmail(), userLoginDTO.getPassword());

            // Persist authentication in the session
            request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return ResponseEntity.ok(authResponse);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/pendingUsers")
    public ResponseEntity<?> getPendingUsers() {
        List<UserDTO> pendingUsers = userService.getNewUsers();
        if (pendingUsers.isEmpty()) {
            return ResponseEntity.ok("No new users pending approval.");
        }
        return ResponseEntity.ok(pendingUsers);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        userService.resetPassword(forgotPasswordDTO);
        return ResponseEntity.ok("Password reset successful.");
    }
}