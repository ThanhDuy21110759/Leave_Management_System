package com.example.management.payload.response;

import com.example.management.entity.ERole;
import com.example.management.entity.Role;

import java.util.HashSet;
import java.util.Set;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Long remainingLeaveDays;
    private Set<Role> roles = new HashSet<>();

    public UserResponse(){

    }

    public UserResponse(Long id, String username, String email, Long remainingLeaveDays, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.remainingLeaveDays = remainingLeaveDays;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRemainingLeaveDays() {
        return remainingLeaveDays;
    }

    public void setRemainingLeaveDays(Long remainingLeaveDays) {
        this.remainingLeaveDays = remainingLeaveDays;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
