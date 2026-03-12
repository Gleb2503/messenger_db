package org.example.service;

import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiKeyService {

    private final UserRepository userRepository;

    public Map<String, Object> getApiKeyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Map<String, Object> response = new HashMap<>();
        response.put("apiKey", user.getApiKey());
        response.put("expiresAt", user.getApiKeyExpiresAt());
        response.put("createdAt", user.getCreatedAt());
        return response;
    }

    @Transactional
    public Map<String, Object> regenerateApiKey(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.generateApiKey();
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("apiKey", user.getApiKey());
        response.put("expiresAt", user.getApiKeyExpiresAt());
        response.put("message", "API key regenerated successfully");
        return response;
    }

    @Transactional
    public void revokeApiKey(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setApiKey(null);
        user.setApiKeyExpiresAt(null);
        userRepository.save(user);
    }
}