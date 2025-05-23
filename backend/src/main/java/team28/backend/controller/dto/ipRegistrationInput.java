package team28.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ipRegistrationInput(
        @NotBlank String MacAddress,
        @NotBlank String ipAddress) {
}