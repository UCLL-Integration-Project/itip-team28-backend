package team28.backend.controller.dto;

import jakarta.validation.constraints.NotNull;

public record CoordinateInput(
        @NotNull int longitude,
        @NotNull int latitude) {
}