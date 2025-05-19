package team28.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReaderInput(
        @NotBlank String MacAddress,
        @NotNull String name,
        @NotBlank String coordinates) {
}
