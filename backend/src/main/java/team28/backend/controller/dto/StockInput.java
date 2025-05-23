package team28.backend.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockInput (
    @NotNull Long itemId,
    @Positive int quantity
){}
