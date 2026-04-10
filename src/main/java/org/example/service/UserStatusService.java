package org.example.service;

import org.example.dto.UserStatus.UserStatusResponse;
import org.example.entity.UserStatus;
import org.example.repository.UserStatusRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusService {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserStatusRepository userStatusRepository;

    private final Map<Long, Boolean> userOnlineStatus = new ConcurrentHashMap<>();

    @Transactional
    public void updateStatus(Long userId, Boolean online) {
        if (userId == null || online == null) return;

        userOnlineStatus.put(userId, online);

        UserStatus status = userStatusRepository.findById(userId)
                .orElse(new UserStatus(userId, false, LocalDateTime.now()));

        status.setOnline(online);
        status.setLastSeen(LocalDateTime.now());
        userStatusRepository.save(status);

        UserStatusResponse response = new UserStatusResponse(userId, online, System.currentTimeMillis());
        messagingTemplate.convertAndSend("/topic/user/" + userId + "/status", response);
        log.info("Status updated for user {}: online={}", userId, online);
    }

    public Boolean isOnline(Long userId) {
        return userOnlineStatus.getOrDefault(userId, false);
    }
}