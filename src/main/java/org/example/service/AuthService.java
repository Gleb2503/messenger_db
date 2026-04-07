package org.example.service;

import org.example.dto.Auth.AuthResponse;
import org.example.dto.Auth.LoginRequest;
import org.example.dto.Auth.RegisterRequest;
import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.UserRepository;
import org.example.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedPhone = normalizePhone(request.getPhone());

        if (normalizedPhone == null || normalizedPhone.isEmpty()) {
            throw new IllegalArgumentException("Некорректный формат номера телефона");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByPhoneNumber(normalizedPhone)) {
            throw new RuntimeException("Phone number already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(normalizedPhone);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName());
        user.setStatus(org.example.enums.UserStatus.offline);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(savedUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser);

        return new AuthResponse(savedUser.getId(), savedUser.getUsername(), token, refreshToken);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String normalizedPhone = normalizePhone(request.getPhone());

        User user = userRepository.findByPhoneNumber(normalizedPhone)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return new AuthResponse(user.getId(), user.getUsername(), token, refreshToken);
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Not a refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtTokenProvider.generateToken(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        return new AuthResponse(user.getId(), user.getUsername(), token, newRefreshToken);
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) return null;
        String cleaned = phone.replaceAll("[^\\d+]", "");
        if (cleaned.startsWith("8") && cleaned.length() == 11) {
            cleaned = "+7" + cleaned.substring(1);
        } else if (cleaned.startsWith("7") && cleaned.length() == 11) {
            cleaned = "+7" + cleaned.substring(1);
        } else if (!cleaned.startsWith("+") && cleaned.length() == 11) {
            cleaned = "+" + cleaned;
        }
        return cleaned;
    }
}