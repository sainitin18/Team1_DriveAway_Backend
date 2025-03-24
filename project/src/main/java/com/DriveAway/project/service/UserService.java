package com.DriveAway.project.service;

import java.util.List;

import com.DriveAway.project.dto.UserDto;
import com.DriveAway.project.model.User;

public interface UserService {
    User registerUser(UserDto userDto);
    List<User> getAllUsers();
    User getUserById(Long userId);
    User updateUser(Long userId, UserDto userDto);
    void deleteUser(Long userId);
}
