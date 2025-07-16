package com.example.eventapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.eventapi.dto.JwtResponse;
import com.example.eventapi.dto.LoginRequest;
import com.example.eventapi.dto.RegisterRequest;
import com.example.eventapi.entity.Role;
import com.example.eventapi.entity.User;
import com.example.eventapi.exception.ResourceNotFoundException;
import com.example.eventapi.repository.UserRepository;
import com.example.eventapi.security.JwtProvider;

@Service
public class AuthService implements UserDetailsService {

    @Autowired private UserRepository userRepo;
    @Autowired @Lazy private AuthenticationManager authManager;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User u = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Not found: " + email));
        return org.springframework.security.core.userdetails.User.builder()
                   .username(u.getEmail())
                   .password(u.getPassword())
                   .roles(u.getRole().name().substring(5)) // drop "ROLE_"
                   .build();
    }

    @Transactional
    public JwtResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Email already in use");
        
        // Debug logging
        System.out.println("DEBUG: Received role from request: '" + req.getRole() + "'");
        System.out.println("DEBUG: Role is null: " + (req.getRole() == null));
        System.out.println("DEBUG: Role is empty: " + (req.getRole() != null && req.getRole().trim().isEmpty()));
        
        // Determine role from request - improved logic
        Role userRole;
        if (req.getRole() != null && "ADMIN".equalsIgnoreCase(req.getRole().trim())) {
            userRole = Role.ROLE_ADMIN;
            System.out.println("DEBUG: Setting role to ROLE_ADMIN");
        } else {
            userRole = Role.ROLE_USER;
            System.out.println("DEBUG: Setting role to ROLE_USER (default or non-ADMIN)");
        }
        
        User u = User.builder()
                     .name(req.getName())
                     .email(req.getEmail())
                     .password(encoder.encode(req.getPassword()))
                     .role(userRole)
                     .build();
        
        System.out.println("DEBUG: About to save user with role: " + userRole.name());
        User savedUser = userRepo.save(u);
        System.out.println("DEBUG: Saved user with role: " + savedUser.getRole().name());

        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        String token = jwtProvider.generateToken(auth);

        JwtResponse response = new JwtResponse(token, "Bearer", savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole().name());
        System.out.println("DEBUG: Returning response with role: " + response.getRole());
        
        return response;
    }

    public JwtResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        String token = jwtProvider.generateToken(auth);
        User u = userRepo.findByEmail(req.getEmail())
                         .orElseThrow(() -> new ResourceNotFoundException("User", "email", req.getEmail()));
        return new JwtResponse(token, "Bearer", u.getId(), u.getName(), u.getEmail(), u.getRole().name());
    }
}
