package org.example.service;

import org.example.dto.User.UserResponse;
import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserResponse> getLast100Users() {
        return userRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return convertToResponse(user);
    }

    public List<UserResponse> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContaining(username)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse createUser(org.example.dto.User.CreateUserRequest request) {
        User user = request.toEntity();
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);
        return convertToResponse(saved);
    }

    @Transactional
    public UserResponse updateUser(Long id, org.example.dto.User.UpdateUserRequest request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (request.getDisplayName() != null) {
            existing.setDisplayName(request.getDisplayName());
        }
        if (request.getPhoneNumber() != null) {
            existing.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAvatarUrl() != null) {
            existing.setAvatarUrl(request.getAvatarUrl());
        }

        User updated = userRepository.save(existing);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setDisplayName(user.getDisplayName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setLastSeen(user.getLastSeen());
        return response;
    }
}