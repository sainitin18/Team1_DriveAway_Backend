package com.DriveAway.project.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.DriveAway.project.dto.AddressDTO;
import com.DriveAway.project.dto.AuthResponseDTO;
import com.DriveAway.project.dto.ForgotPasswordDTO;
import com.DriveAway.project.dto.UserDTO;
import com.DriveAway.project.dto.UserResponseDTO;
import com.DriveAway.project.exception.UserAlreadyExistsException;
import com.DriveAway.project.exception.UserNotFoundException;
import com.DriveAway.project.model.Address;
import com.DriveAway.project.model.User;
import com.DriveAway.project.repository.UserRepository;

import jakarta.transaction.Transactional;


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
        if (userDTO.getAddress() != null) {
            Address address = modelMapper.map(userDTO.getAddress(), Address.class);
            address.setUser(user);  
            user.setAddress(address);
        }
        return userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
//                    .filter(user -> "ACCEPTED".equalsIgnoreCase(user.getStatus()))
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .collect(Collectors.toList());
    }
    
    @Override
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setAadharNumber(user.getAadharNumber());
        dto.setDrivingLicense(user.getDrivingLicense());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setAltMobileNumber(user.getAltMobileNumber());

        // Setting nested address DTO
        UserResponseDTO.AddressDTO addressDTO = new UserResponseDTO.AddressDTO();
        addressDTO.setStreet(user.getAddress().getStreet());
        addressDTO.setCity(user.getAddress().getCity());
        addressDTO.setState(user.getAddress().getState());
        addressDTO.setPostalCode(user.getAddress().getPostalCode());
        addressDTO.setCountry(user.getAddress().getCountry());

        dto.setAddress(addressDTO);

        return dto;
    }

    
    @Override
    public UserResponseDTO getUserByEmail(String email) {
    	User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + email));
        
    	 UserResponseDTO dto = new UserResponseDTO();
         dto.setEmail(user.getEmail());
         dto.setUsername(user.getUsername());
         dto.setAadharNumber(user.getAadharNumber());
         dto.setDrivingLicense(user.getDrivingLicense());
         dto.setMobileNumber(user.getMobileNumber());
         dto.setAltMobileNumber(user.getAltMobileNumber());

         // Setting nested address DTO
         UserResponseDTO.AddressDTO addressDTO = new UserResponseDTO.AddressDTO();
         addressDTO.setStreet(user.getAddress().getStreet());
         addressDTO.setCity(user.getAddress().getCity());
         addressDTO.setState(user.getAddress().getState());
         addressDTO.setPostalCode(user.getAddress().getPostalCode());
         addressDTO.setCountry(user.getAddress().getCountry());

         dto.setAddress(addressDTO);

         return dto;
    }

    @Override
    public User updateUser(Long userId, UserResponseDTO userResponseDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Update basic user fields
        user.setEmail(userResponseDTO.getEmail());
        user.setUsername(userResponseDTO.getUsername());
        user.setAadharNumber(userResponseDTO.getAadharNumber());
        user.setDrivingLicense(userResponseDTO.getDrivingLicense());
        user.setMobileNumber(userResponseDTO.getMobileNumber());
        user.setAltMobileNumber(userResponseDTO.getAltMobileNumber());

        // Update address if available in DTO
        if (userResponseDTO.getAddress() != null) {
            UserResponseDTO.AddressDTO addressDTO = userResponseDTO.getAddress();
            Address address = user.getAddress();

            if (address == null) {
                address = new Address();
                address.setUser(user);
            }

            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setPostalCode(addressDTO.getPostalCode());
            address.setCountry(addressDTO.getCountry());

            user.setAddress(address);
        }

        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // No need to nullify manually
        userRepository.delete(user);
    }


	@Override
	public void updateUserStatus(Long userId, String newUserStatus) {
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
		user.setStatus(newUserStatus);
        userRepository.save(user);;
	}
	
//	public boolean authenticateUser(String email, String rawPassword) {	    
//	    User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//	    // Compare entered password with the hashed password in DB
//	    return passwordEncoder.matches(rawPassword, user.getPassword());
//	}
	
	public AuthResponseDTO authenticateUser(String email, String rawPassword) {
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));

	    String status = user.getStatus();

	    if ("PENDING".equalsIgnoreCase(status)) {
	        throw new IllegalStateException("Your account is pending approval.");
	    }

	    if ("REJECTED".equalsIgnoreCase(status)) {
	        throw new IllegalStateException("Your account has been rejected. You are not allowed to log in.");
	    }

	    boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());

	    if (matches) {
	        org.springframework.security.core.Authentication authentication = new UsernamePasswordAuthenticationToken(
	                user.getEmail(),
	                null,
	                Collections.singleton(new SimpleGrantedAuthority(user.getRole())) 
	        );

	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        return new AuthResponseDTO(user.getUserId(), user.getUsername(), user.getRole(), user.getEmail());
	    } else {
	        throw new IllegalArgumentException("Invalid password");
	    }
	}
	
	@Override
	public List<UserDTO> getNewUsers() {
	    List<User> pendingUsers = userRepository.findByStatus("PENDING");
	    return pendingUsers.stream()
	            .map(user -> modelMapper.map(user, UserDTO.class))
	            .collect(Collectors.toList());
	}
	
	@Override
	public void resetPassword(ForgotPasswordDTO forgotPasswordDTO) {
	    User user = userRepository.findByEmail(forgotPasswordDTO.getEmail())
	            .orElseThrow(() -> new UserNotFoundException("User not found"));

	    // Only allow password reset for users with ACCEPTED status
	    if (!"ACCEPTED".equalsIgnoreCase(user.getStatus())) {
	        throw new IllegalStateException("Password reset is allowed only for accepted users.");
	    }

	    String encryptedPassword = passwordEncoder.encode(forgotPasswordDTO.getNewPassword());
	    user.setPassword(encryptedPassword);

	    userRepository.save(user);
	}
}  