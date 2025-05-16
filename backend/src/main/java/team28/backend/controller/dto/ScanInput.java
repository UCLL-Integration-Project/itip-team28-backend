package team28.backend.controller.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import team28.backend.model.Car;
import team28.backend.model.Tag;

public record ScanInput(
        @NotNull Car carId,
        @NotNull Tag tagId,
        @NotNull LocalDateTime timestamp) {
}
