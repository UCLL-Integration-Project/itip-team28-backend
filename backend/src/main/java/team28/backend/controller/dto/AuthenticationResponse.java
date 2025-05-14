package team28.backend.controller.dto;

import team28.backend.model.Role;

public record AuthenticationResponse(
                String message,
                String token,
                String username,
                Role role,
                Long id) {

}
