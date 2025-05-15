package team28.backend.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import team28.backend.model.Role;

public record UserInput(
                @NotBlank String username,
                @Email @NotNull(message = "Email cannot be empty.") String email,
                @NotBlank String password,
                Role role) {
}
