package team28.backend.controller.dto;

public record ChangePasswordRequest (
    String oldPassword,
    String newPassword,
    String confirmNewPassword
){}
