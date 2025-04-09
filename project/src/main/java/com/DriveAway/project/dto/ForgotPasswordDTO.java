package com.DriveAway.project.dto;

import lombok.Data;

@Data
public class ForgotPasswordDTO {
    private String email;
    private String newPassword;
}