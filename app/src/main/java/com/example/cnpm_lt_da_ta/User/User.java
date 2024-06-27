package com.example.cnpm_lt_da_ta.User;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String email;
    private String role;
    private String name;
    private String phone;

    public User() {
    }

    public User(String userId, String email, String role, String name, String phone) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.name = name;
        this.phone =phone;
    }
    public  String getUserId() {return userId; }

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
    public  void setUserId(String userId) {this.userId = userId;}
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
