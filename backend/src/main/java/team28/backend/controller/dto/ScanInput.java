package team28.backend.controller.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record ScanInput(
        @NotNull Long carId,
        @NotNull Long tagId,
        @NotNull LocalDateTime timestamp) {
}
