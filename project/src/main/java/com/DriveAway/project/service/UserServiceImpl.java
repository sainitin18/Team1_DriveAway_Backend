package com.DriveAway.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.DriveAway.project.dto.UserDTO;
import com.DriveAway.project.exception.UserAlreadyExistsException;
import com.DriveAway.project.exception.UserNotFoundException;
import com.DriveAway.project.model.User;
import com.DriveAway.project.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User registerUser(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        
        // Encrypt the password before saving
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encryptedPassword);
        
        user.setAadharNumber(userDTO.getAadharNumber());
        user.setDrivingLicense(userDTO.getDrivingLicense());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setAltMobileNumber(userDTO.getAltMobileNumber());
        return userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .collect(Collectors.toList());
    }
    
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public User updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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

	@Override
	public void updateUserStatus(Long userId, String newUserStatus) {
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
		user.setStatus(newUserStatus);
        userRepository.save(user);;
	}
	
	public boolean authenticateUser(String email, String rawPassword) {	    
	    User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

	    // Compare entered password with the hashed password in DB
	    return passwordEncoder.matches(rawPassword, user.getPassword());
	}
}
