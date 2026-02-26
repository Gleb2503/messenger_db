package org.example.repository;

import org.example.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByUser_Id(Long userId);

    List<Session> findByUser_IdAndIsActive(Long userId, Boolean isActive);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}