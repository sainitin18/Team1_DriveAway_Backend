package com.DriveAway.project.service;

import java.util.List;

import com.DriveAway.project.dto.AuthResponseDTO;
import com.DriveAway.project.dto.ForgotPasswordDTO;
import com.DriveAway.project.dto.UserDTO;
import com.DriveAway.project.model.User;

public interface UserService {
	User registerUser(UserDTO userDto);
    List<UserDTO> getAllUsers();
    User getUserById(Long userId);
    User updateUser(Long userId, UserDTO userDto);
    void deleteUser(Long userId);
    void updateUserStatus(Long userId, String newStatus);
    public AuthResponseDTO authenticateUser(String email, String rawPassword);
	List<UserDTO> getNewUsers();
	public void resetPassword(ForgotPasswordDTO forgotPasswordDTO);
}
