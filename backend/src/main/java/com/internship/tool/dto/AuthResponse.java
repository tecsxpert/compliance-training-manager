package com.internship.tool.dto;


public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String token, String username, String role, String message) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.message = message;
    }

    public String getToken() { return this.token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return this.role; }
    public void setRole(String role) { this.role = role; }

    public String getMessage() { return this.message; }
    public void setMessage(String message) { this.message = message; }

}
