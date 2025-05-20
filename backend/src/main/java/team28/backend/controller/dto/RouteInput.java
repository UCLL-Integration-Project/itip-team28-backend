package team28.backend.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public record RouteInput(
        boolean status,
        @NotNull Long startingPointReaderId,
        @NotNull Long destinationReaderId,
        @NotNull Long currentPointReaderId,
        @NotNull LocalDateTime timestamp,
        List<String> instructions) {
}
