package team28.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ItemInput (
    @NotBlank String name
){}
