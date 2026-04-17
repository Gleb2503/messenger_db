package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserStatus.UserStatusResponse;
import org.example.entity.UserStatus;
import org.example.repository.UserStatusRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusService {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserStatusRepository userStatusRepository;


    private final ConcurrentMap<Long, UserStatusCache> statusCache = new ConcurrentHashMap<>();


    private static final long INACTIVITY_TIMEOUT_MS = 30_000;


    private static class UserStatusCache {
        boolean online;
        LocalDateTime lastSeen;

        UserStatusCache(boolean online) {
            this.online = online;
            this.lastSeen = LocalDateTime.now();
        }

        void touch() {
            this.lastSeen = LocalDateTime.now();
        }

        boolean isTimedOut() {
            return !online &&
                    lastSeen.plus(Duration.ofMillis(INACTIVITY_TIMEOUT_MS))
                            .isBefore(LocalDateTime.now());
        }
    }

    public void updateStatus(Long userId, Boolean online) {
        if (userId == null || online == null) {
            log.warn("Invalid status update: userId={}, online={}", userId, online);
            return;
        }

        UserStatusCache cache = statusCache.compute(userId, (k, v) -> {
            if (v == null) return new UserStatusCache(online);
            v.online = online;
            v.touch();
            return v;
        });

        UserStatusResponse response = new UserStatusResponse(userId, online, System.currentTimeMillis());
        log.info("📤 Broadcasting status: user={} online={}", userId, online);
        messagingTemplate.convertAndSend("/topic/user/" + userId + "/status", response);
    }

    public boolean isOnline(Long userId) {
        if (userId == null) return false;

        UserStatusCache cache = statusCache.get(userId);
        if (cache == null) {
            return userStatusRepository.findById(userId)
                    .map(UserStatus::getOnline)
                    .orElse(false);
        }


        if (!cache.online && cache.isTimedOut()) {
            statusCache.remove(userId); // Очищаем устаревшую запись
            return false;
        }

        return cache.online;
    }




    public void handleStatusRequest(Long targetUserId, Long requesterId) {
        if (targetUserId == null || requesterId == null) return;

        boolean currentStatus = isOnline(targetUserId);

        String username = requesterId.toString();
        log.info("📤 Sending response: requester={}, username={}, destination=/queue/user.status",
                requesterId, username);

        UserStatusResponse response = new UserStatusResponse(
                targetUserId,
                currentStatus,
                System.currentTimeMillis()
        );

        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/user.status",
                response
        );
        log.info("📤 Sending duplicate to public topic: /topic/user/{}/status", targetUserId);
        messagingTemplate.convertAndSend("/topic/user/" + targetUserId + "/status", response);
    }


    @Scheduled(fixedRate = 10_000)
    public void cleanupTimedOutStatuses() {
        statusCache.entrySet().removeIf(entry -> {
            UserStatusCache cache = entry.getValue();
            if (cache.isTimedOut()) {
                Long userId = entry.getKey();
                log.info("🧹 Cleaned up timed-out status for user {}", userId);

                UserStatusResponse response = new UserStatusResponse(
                        userId,
                        false,
                        System.currentTimeMillis()
                );
                messagingTemplate.convertAndSend("/topic/user/" + userId + "/status", response);

                return true;
            }
            return false;
        });
    }


    public void onUserConnected(Long userId) {
        log.info("🔌 User connected: {}", userId);
        updateStatus(userId, true);
    }


    public void onUserDisconnected(Long userId) {
        log.info("🔌 User disconnected: {}", userId);

    }
}