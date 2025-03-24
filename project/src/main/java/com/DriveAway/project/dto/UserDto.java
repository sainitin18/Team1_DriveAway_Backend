package com.DriveAway.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Username should contain only letters")
    private String username;

    @NotBlank(message = "Password is required")
//    @Pattern(regexp = "^(?=.[A-Z])(?=.\\d)(?=.[@$!%?&])[A-Za-z\\d@$!%*?&]{8,}$", 
//    message = "Password must have at least one uppercase letter, one number, one special character and be 8+ characters long")
//    @Pattern(
//        regexp = "^(?=.[A-Z])(?=.\\d)(?=.[@$!%?&])[A-Za-z\\d@$!%*?&]{8,}$",
//        message = "Password must have at least one uppercase letter, one number, one special character, and be 8+ characters long"
//    )
    private String password;

    @NotBlank(message = "Aadhar number is required")
    @Pattern(regexp = "^[2-9]{1}[0-9]{11}$", message = "Invalid Aadhar number")
    private String aadharNumber;

    @NotBlank(message = "Driving License number is required")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{13}$", message = "Invalid Driving License number")
    private String drivingLicense;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]{1}[0-9]{9}$", message = "Invalid Mobile number")
    private String mobileNumber;

    @Pattern(regexp = "^[6-9]{1}[0-9]{9}$", message = "Invalid Alternative Mobile number")
    private String altMobileNumber;
}