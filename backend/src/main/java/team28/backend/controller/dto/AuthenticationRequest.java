package team28.backend.controller.dto;

public record AuthenticationRequest(
                String username,
                String password) {
}