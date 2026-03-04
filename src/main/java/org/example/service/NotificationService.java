package org.example.service;

import org.example.dto.Notification.NotificationResponse;
import org.example.dto.Notification.CreateNotificationRequest;
import org.example.dto.User.UserDTO;
import org.example.entity.Notification;
import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.NotificationRepository;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationResponse> getLast100Notifications() {
        return notificationRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getLast100NotificationsByUser(Long userId) {
        return notificationRepository.findTop100ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    public List<NotificationResponse> getLast100UnreadByUser(Long userId) {
        return notificationRepository.findTop100ByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return convertToResponse(notification);
    }

    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        Notification notification = request.toEntity();

        if (notification.getUser() != null && notification.getUser().getId() != null) {
            User user = userRepository.findById(notification.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + notification.getUser().getId()));
            notification.setUser(user);
        }

        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        return convertToResponse(saved);
    }

    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        Notification updated = notificationRepository.save(notification);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }

    private NotificationResponse convertToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setType(notification.getType());
        response.setTitle(notification.getTitle());
        response.setBody(notification.getBody());
        response.setEntityId(notification.getEntityId());
        response.setEntityType(notification.getEntityType());
        response.setIsRead(notification.getIsRead());
        response.setCreatedAt(notification.getCreatedAt());
        response.setReadAt(notification.getReadAt());

        if (notification.getUser() != null) {
            response.setUser(new UserDTO(
                    notification.getUser().getId(),
                    notification.getUser().getUsername(),
                    notification.getUser().getEmail()
            ));
        }

        return response;
    }
}