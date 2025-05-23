package team28.backend.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record GridInput(
        @NotEmpty List<@NotNull CoordinateInput> coordinates) {
}
