package com.DriveAway.project.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
	 private String email;
	    private String username;
	    private String aadharNumber;
	    private String drivingLicense;
	    private String mobileNumber;
	    private String altMobileNumber;
	    
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String country;
}
