package org.example.service;

import org.example.entity.UserSettings;
import org.example.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;

    public Optional<UserSettings> getSettingsByUserId(Long userId) {
        return userSettingsRepository.findByUser_Id(userId);
    }

    @Transactional
    public UserSettings createSettings(UserSettings settings) {
        return userSettingsRepository.save(settings);
    }

    @Transactional
    public UserSettings updateSettings(Long userId, UserSettings settingsDetails) {
        return userSettingsRepository.findByUser_Id(userId)
                .map(settings -> {
                    settings.setPushEnabled(settingsDetails.getPushEnabled());
                    settings.setEmailEnabled(settingsDetails.getEmailEnabled());
                    settings.setSoundEnabled(settingsDetails.getSoundEnabled());
                    settings.setTheme(settingsDetails.getTheme());
                    settings.setLanguage(settingsDetails.getLanguage());
                    return userSettingsRepository.save(settings);
                })
                .orElseGet(() -> {
                    settingsDetails.getUser().setId(userId);
                    return userSettingsRepository.save(settingsDetails);
                });
    }

    @Transactional
    public void deleteSettings(Long userId) {
        userSettingsRepository.findByUser_Id(userId)
                .ifPresent(settings -> userSettingsRepository.delete(settings));
    }
}