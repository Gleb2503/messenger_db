package org.example.service;

import org.example.entity.Session;
import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public List<Session> getLast100SessionsByUser(Long userId) {
        return sessionRepository.findTop100ByUserIdOrderByLastActiveAtDesc(userId);
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
    }

    @Transactional
    public Session createSession(Session session) {
        if (session.getUser() != null && session.getUser().getId() != null) {
            User user = userRepository.findById(session.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + session.getUser().getId()));
            session.setUser(user);
        }
        session.setIsActive(true);
        session.setLastActiveAt(LocalDateTime.now());
        session.setCreatedAt(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    @Transactional
    public Session updateLastActive(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
        session.setLastActiveAt(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    @Transactional
    public void revokeSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
        session.setIsActive(false);
        sessionRepository.save(session);
    }

    @Transactional
    public void revokeAllUserSessions(Long userId) {
        List<Session> sessions = sessionRepository.findTop100ByUserIdAndIsActiveTrueOrderByLastActiveAtDesc(userId);
        for (Session session : sessions) {
            session.setIsActive(false);
        }
        sessionRepository.saveAll(sessions);
    }
}