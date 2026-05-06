package com.internship.tool.auth;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // REGISTER
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        // simulate hashing (for now)
        String hashedPassword = "hashed_" + password;

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User registered successfully");
        response.put("email", email);
        response.put("role", "VIEWER");

        return response;
    }

    // LOGIN
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        // simulate validation
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new RuntimeException("Email or password cannot be empty");
        }

        // simulate JWT (structured, not dumb string)
        String token = "jwt_token_for_" + email;

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("token", token);

        return response;
    }

    // REFRESH TOKEN
    @PostMapping("/refresh")
    public Map<String, Object> refresh() {

        String newToken = "refreshed_jwt_token";

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("token", newToken);

        return response;
    }
}