package org.example.service;

import org.example.entity.Session;
import org.example.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public List<Session> getSessionsByUserId(Long userId) {
        return sessionRepository.findByUser_Id(userId);
    }

    public List<Session> getActiveSessionsByUserId(Long userId) {
        return sessionRepository.findByUser_IdAndIsActive(userId, true);
    }

    @Transactional
    public Session createSession(Session session) {
        session.setIsActive(true);
        session.setLastActiveAt(LocalDateTime.now());
        session.setCreatedAt(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    @Transactional
    public Session updateLastActive(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setLastActiveAt(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    @Transactional
    public void logoutSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setIsActive(false);
        sessionRepository.save(session);
    }

    @Transactional
    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    @Transactional
    public void logoutAllUserSessions(Long userId) {
        sessionRepository.findByUser_Id(userId)
                .forEach(session -> {
                    session.setIsActive(false);
                    sessionRepository.save(session);
                });
    }

    @Transactional
    public void deleteExpiredSessions() {
        sessionRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}