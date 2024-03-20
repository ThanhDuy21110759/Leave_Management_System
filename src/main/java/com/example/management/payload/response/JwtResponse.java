package com.example.management.payload.response;

import com.example.management.entity.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private List<String> roles = new ArrayList<>();

    public JwtResponse(String token, List<String> roles) {
        this.token = token;
        this.roles = roles;
    }

    public String getAccessToken() {
      return token;
    }
    public void setAccessToken(String accessToken) {
      this.token = accessToken;
    }
    public String getTokenType() {
      return type;
    }
    public void setTokenType(String tokenType) {
      this.type = tokenType;
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
