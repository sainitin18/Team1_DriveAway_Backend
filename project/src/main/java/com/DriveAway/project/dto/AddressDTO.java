package com.DriveAway.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddressDTO {

    private Long addressId;  // ✅ ID will be returned in responses

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "City should contain only letters and spaces")
    private String city;

    @NotBlank(message = "State is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "State should contain only letters and spaces")
    private String state;

    @NotBlank(message = "Postal Code is required")
    @Pattern(regexp = "^[1-9]{1}[0-9]{5}$", message = "Invalid Postal Code (should be 6 digits)")
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Country should contain only letters and spaces")
    private String country;

    private Long userId; // Foreign key reference to User
}
