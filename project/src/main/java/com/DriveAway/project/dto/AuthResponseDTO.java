package com.DriveAway.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
	private Long userId;
    private String userName;
    private String role;
}