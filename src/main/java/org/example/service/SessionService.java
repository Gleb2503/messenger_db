package org.example.service;

import org.example.dto.Session.SessionResponse;
import org.example.dto.Session.CreateSessionRequest;
import org.example.dto.User.UserDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public List<SessionResponse> getLast100SessionsByUser(Long userId) {
        return sessionRepository.findTop100ByUserIdOrderByLastActiveAtDesc(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public SessionResponse getSessionById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
        return convertToResponse(session);
    }

    @Transactional
    public SessionResponse createSession(CreateSessionRequest request) {
        Session session = request.toEntity();

        if (session.getUser() != null && session.getUser().getId() != null) {
            User user = userRepository.findById(session.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + session.getUser().getId()));
            session.setUser(user);
        }

        session.setIsActive(true);
        session.setLastActiveAt(LocalDateTime.now());
        session.setCreatedAt(LocalDateTime.now());

        Session saved = sessionRepository.save(session);
        return convertToResponse(saved);
    }

    @Transactional
    public SessionResponse updateLastActive(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
        session.setLastActiveAt(LocalDateTime.now());
        Session updated = sessionRepository.save(session);
        return convertToResponse(updated);
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

    private SessionResponse convertToResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setDeviceName(session.getDeviceName());
        response.setIpAddress(session.getIpAddress());
        response.setUserAgent(session.getUserAgent());
        response.setIsActive(session.getIsActive());
        response.setCreatedAt(session.getCreatedAt());
        response.setLastActiveAt(session.getLastActiveAt());
        response.setExpiresAt(session.getExpiresAt());

        if (session.getUser() != null) {
            response.setUser(new UserDTO(
                    session.getUser().getId(),
                    session.getUser().getUsername(),
                    session.getUser().getEmail()
            ));
        }

        return response;
    }
}