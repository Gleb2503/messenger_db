package org.example.service;

import org.example.entity.UserSettings;
import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.UserSettingsRepository;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserRepository userRepository;

    public List<UserSettings> getAllSettings() {
        return userSettingsRepository.findAll();
    }

    public UserSettings getSettingsById(Long id) {
        return userSettingsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Settings not found with id: " + id));
    }

    public UserSettings getSettingsByUser(Long userId) {
        return userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Settings not found for user: " + userId));
    }

    @Transactional
    public UserSettings createSettings(UserSettings settings) {
        if (settings.getUser() != null && settings.getUser().getId() != null) {
            User user = userRepository.findById(settings.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + settings.getUser().getId()));
            settings.setUser(user);
        }
        return userSettingsRepository.save(settings);
    }

    @Transactional
    public UserSettings updateSettings(Long userId, UserSettings settings) {
        UserSettings existingSettings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Settings not found for user: " + userId));

        existingSettings.setPushEnabled(settings.getPushEnabled());
        existingSettings.setEmailEnabled(settings.getEmailEnabled());
        existingSettings.setSoundEnabled(settings.getSoundEnabled());
        existingSettings.setTheme(settings.getTheme());
        existingSettings.setLanguage(settings.getLanguage());
        existingSettings.setUpdatedAt(LocalDateTime.now());

        return userSettingsRepository.save(existingSettings);
    }

    @Transactional
    public void deleteSettings(Long userId) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Settings not found for user: " + userId));
        userSettingsRepository.delete(settings);
    }
}