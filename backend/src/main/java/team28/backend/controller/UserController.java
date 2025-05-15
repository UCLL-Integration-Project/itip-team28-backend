package team28.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import team28.backend.controller.dto.AuthenticationRequest;
import team28.backend.controller.dto.AuthenticationResponse;
import team28.backend.controller.dto.UserInput;
import team28.backend.model.User;
import team28.backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService UserService;

    public UserController(UserService UserService) {
        this.UserService = UserService;
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of users returned successfully")
    @GetMapping
    public List<User> GetAllUsers() {
        return UserService.GetAllUsers();
    }

    @Operation(summary = "Update an existing user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping
    public User UpdateUser(@RequestBody User UpdatedUser, @PathVariable Long id) {
        return UserService.UpdateUser(id, UpdatedUser);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping
    public String DeleteUser(@RequestBody Long id) {
        UserService.DeleteUser(id);
        return "User deleted";
    }

    @Operation(summary = "Login with username and password")
    @ApiResponse(responseCode = "200", description = "Authentication successful")
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
        return UserService.Login(authenticationRequest.username(), authenticationRequest.password());
    }

    @Operation(summary = "Sign up a new user")
    @ApiResponse(responseCode = "200", description = "User signed up successfully")
    @PostMapping("/signup")
    public User Signup(@Valid @RequestBody UserInput userInput) {
        return UserService.Signup(userInput);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return errors;
    }
}