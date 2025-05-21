package team28.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record IpRegistrationInput(
        @NotBlank String MacAddress,
        @NotBlank String ipAddress) {
}