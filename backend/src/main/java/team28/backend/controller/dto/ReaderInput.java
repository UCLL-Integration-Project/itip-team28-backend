package team28.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import team28.backend.model.Coordinate;

public record ReaderInput(
        @NotBlank String macAddress,
        @NotNull String name,
        @NotNull Coordinate coordinates) {
}
