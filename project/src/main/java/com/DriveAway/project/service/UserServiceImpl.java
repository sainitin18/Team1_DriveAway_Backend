package com.DriveAway.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DriveAway.project.dto.UserDto;
import com.DriveAway.project.exception.UserAlreadyExistsException;
import com.DriveAway.project.exception.UserNotFoundException;
import com.DriveAway.project.model.User;
import com.DriveAway.project.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(UserDto userDTO) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setAadharNumber(userDTO.getAadharNumber());
        user.setDrivingLicense(userDTO.getDrivingLicense());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setAltMobileNumber(userDTO.getAltMobileNumber());
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public User updateUser(Long userId, UserDto userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setAadharNumber(userDTO.getAadharNumber());
        user.setDrivingLicense(userDTO.getDrivingLicense());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setAltMobileNumber(userDTO.getAltMobileNumber());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
