package org.example.repository;

import org.example.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser_IdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUser_IdAndIsRead(Long userId, Boolean isRead);

    Long countByUser_IdAndIsRead(Long userId, Boolean isRead);
}