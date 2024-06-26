package com.example.cnpm_lt_da_ta.User;

public class User {
    private String email;
    private String role;
    private String name;

    public User() {
    }

    public User(String email, String role, String name) {
        this.email = email;
        this.role = role;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }
}
