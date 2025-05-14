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

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message="Name cannot be empty.")
    private String name;

    @NotNull(message="Lastname cannot be empty.")
    private String lastname;

    @NotNull(message="Username cannot be empty.")
    private String username;

    @Email(message="Email is not valid")
    @NotNull(message="Email cannot be empty.")
    private String email;

    @NotNull(message="Password cannot be empty.")
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    protected User() {}

    public User(String name, String lastname, String username, String email, String password, Role role){
        this.name = name;
        this.lastname = lastname;
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

    public String GetName() {
        return name;
    }

    public void SetName(String name) {
        this.name = name;
    }

    public String GetLastname() {
        return lastname;
    }

    public void SetLastname(String lastname) {
        this.lastname = lastname;
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

    @JsonIgnore
    public String GetFullName() {
        return name + " " + lastname;
    }
}

