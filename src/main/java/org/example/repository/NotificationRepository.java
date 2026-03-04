package org.example.repository;

import org.example.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    List<Notification> findTop100ByUserIdOrderByCreatedAtDesc(Long userId);


    List<Notification> findTop100ByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);


    List<Notification> findTop100ByOrderByCreatedAtDesc();

    boolean existsByUserId(Long userId);
}