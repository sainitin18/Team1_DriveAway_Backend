package com.DriveAway.project.dto;

import java.util.List;

import jakarta.validation.constraints.Max;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VehicleDTO {

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Year is required")
    @Min(value = 1886, message = "Invalid year, must be after 1886")
    private Integer year;

    @NotBlank(message = "Fuel type is required")
    private String fuelType;

    @NotBlank(message = "Transmission type is required")
    private String transmission;

    @NotBlank(message = "Number plate is required")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$", message = "Invalid number plate format")
    private String numberPlate;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    private Double price;

    private String status = "available"; // Default status

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Seater count is required")
    @Min(value = 2, message = "Seater must be 2, 5, or 7")
    @Max(value = 7, message = "Seater must be 2, 5, or 7")
    private int seater;  // Changed from String to Integer

    @NotNull(message = "Security amount is required")
    @Min(value = 0, message = "Security amount must be non-negative")
    private Double securityAmount;
    
    private List<String> imageUrls;
    private CarFeatureDTO carFeature;
}
