package com.internship.tool.service;

import com.internship.tool.dto.AuthResponse;
import com.internship.tool.dto.RegisterRequest;
import com.internship.tool.dto.LoginRequest;
import com.internship.tool.entity.Role;
import com.internship.tool.entity.User;
import com.internship.tool.repository.RoleRepository;
import com.internship.tool.repository.UserRepository;
import com.internship.tool.security.CustomUserDetailsService;
import com.internship.tool.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        // Ensure VIEWER role exists
        if (roleRepository.findByName("VIEWER").isEmpty()) {
            Role r = new Role();
            r.setName("VIEWER");
            roleRepository.save(r);
        }
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role r = new Role();
            r.setName("ADMIN");
            roleRepository.save(r);
        }
    }

    // ✅ TEST REGISTER
    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("testuser@test.com");
        request.setPassword("password123");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("VIEWER", response.getRole());
        assertTrue(response.getToken().length() > 10);
    }

    // ✅ TEST REGISTER DUPLICATE USERNAME
    @Test
    void testRegisterDuplicateUsername() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("dupuser");
        request.setEmail("dup@test.com");
        request.setPassword("password123");
        authService.register(request);

        RegisterRequest duplicate = new RegisterRequest();
        duplicate.setUsername("dupuser");
        duplicate.setEmail("other@test.com");
        duplicate.setPassword("password456");

        assertThrows(IllegalArgumentException.class, () -> authService.register(duplicate));
    }

    // ✅ TEST LOGIN
    @Test
    void testLoginSuccess() {
        // First register
        RegisterRequest reg = new RegisterRequest();
        reg.setUsername("loginuser");
        reg.setEmail("login@test.com");
        reg.setPassword("secure123");
        authService.register(reg);

        // Then login
        LoginRequest login = new LoginRequest();
        login.setUsername("loginuser");
        login.setPassword("secure123");

        AuthResponse response = authService.login(login);
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("loginuser", response.getUsername());
    }

    // ✅ TEST JWT TOKEN IS VALID
    @Test
    void testJwtTokenIsValid() {
        RegisterRequest reg = new RegisterRequest();
        reg.setUsername("jwtuser");
        reg.setEmail("jwt@test.com");
        reg.setPassword("jwt12345");
        AuthResponse response = authService.register(reg);

        String token = response.getToken();
        String username = jwtUtil.extractUsername(token);
        assertEquals("jwtuser", username);

        var userDetails = userDetailsService.loadUserByUsername("jwtuser");
        assertTrue(jwtUtil.isTokenValid(token, userDetails));
    }
}
