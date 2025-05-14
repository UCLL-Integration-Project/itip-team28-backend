package team28.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username cannot be empty.")
    private String username;

    @Email(message = " E mail is not valid")
    @NotNull(message = "Email cannot be empty.")
    private String email;

    @NotNull(message = "Password cannot be empty.")
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    protected User() {
    }

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long GetId() {
        return id;
    }

    public void SetId(Long id) {
        this.id = id;
    }

    public String GetUsername() {
        return username;
    }

    public void SetUsername(String username) {
        this.username = username;
    }

    public String GetEmail() {
        return email;
    }

    public void SetEmail(String email) {
        this.email = email;
    }

    public String GetPassword() {
        return password;
    }

    public void SetPassword(String password) {
        this.password = password;
    }

    public Role GetRole() {
        return role;
    }

    public void SetRole(Role role) {
        this.role = role;
    }

}