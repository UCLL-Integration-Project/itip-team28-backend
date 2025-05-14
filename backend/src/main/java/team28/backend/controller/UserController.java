package team28.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService UserService;

    public UserController(UserService UserService) {
        this.UserService = UserService;
    }

    @GetMapping
    public List<User> GetAllUsers() {
        return UserService.GetAllUsers();
    }

    @PostMapping
    public User CreateUser(@RequestBody User user) {
        return UserService.CreateUser(user);
    }

    @PutMapping
    public User UpdateUser(@RequestBody User UpdatedUser, @RequestBody Long id) {
        return UserService.UpdateUser(id, UpdatedUser);
    }

    @DeleteMapping
    public String DeleteUser(@RequestBody Long id) {
        UserService.DeleteUser(id);
        return "User deleted";
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest AuthenticationRequest) {
        return UserService.Login(AuthenticationRequest.username(),
                AuthenticationRequest.password());
    }

    @PostMapping("/signup")
    public User Signup(@Valid @RequestBody UserInput UserInput) {
        return UserService.Signup(UserInput);
    }

}