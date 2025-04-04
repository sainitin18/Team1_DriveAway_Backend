package com.DriveAway.project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.DriveAway.project.model.User;

@DataJpaTest
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void testSaveUser() {
        User user = new User();
        user.setEmail("testuser@example.com");
        user.setUsername("TestUser");
        user.setPassword("TestPassword");
        user.setAadharNumber("234567890123");
        user.setDrivingLicense("AB1234567890123");
        user.setMobileNumber("9876543210");
        user.setAltMobileNumber("8765432109");
        user.setRole("USER");
        user.setStatus("Active");
        
        User savedUser = userRepository.save(user);
        
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isNotNull();
    }
    
    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("findme@example.com");
        user.setUsername("FindMe");
        user.setPassword("FindPassword");
        user.setAadharNumber("345678901234");
        user.setDrivingLicense("CD1234567890123");
        user.setMobileNumber("8765432109");
        user.setAltMobileNumber("7654321098");
        user.setRole("USER");
        user.setStatus("Pending");
        
        userRepository.save(user);
        
        Optional<User> foundUser = userRepository.findByEmail("findme@example.com");
        
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("FindMe");
    }
}
