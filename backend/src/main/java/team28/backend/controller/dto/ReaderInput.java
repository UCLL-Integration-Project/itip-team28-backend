package team28.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReaderInput(
        @NotBlank String macAddress,
        @NotNull String name,
        @NotNull CoordinateInput coordinates) {
}
