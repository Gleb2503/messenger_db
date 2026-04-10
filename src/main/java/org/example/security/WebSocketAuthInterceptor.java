package org.example.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        SimpMessageType messageType = accessor.getMessageType();

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorization = accessor.getFirstNativeHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                log.warn("WebSocket CONNECT without Authorization header");
                return null;
            }

            String token = authorization.substring(7);

            try {
                if (!jwtTokenProvider.validateToken(token)) {
                    log.warn("WebSocket CONNECT with invalid token");
                    return null;
                }

                Long userId = jwtTokenProvider.getUserIdFromToken(token);
                String username = jwtTokenProvider.getUsernameFromToken(token);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userId, null, null);


                accessor.setUser(auth);

                if (accessor.getSessionAttributes() != null) {
                    accessor.getSessionAttributes().put("userId", userId);
                    accessor.getSessionAttributes().put("username", username);
                }

                log.info("✅ WebSocket authenticated: userId={}, username={}", userId, username);

            } catch (Exception e) {
                log.error("❌ WebSocket authentication error: {}", e.getMessage(), e);
                return null;
            }
        }

        else {
            if (accessor.getUser() == null) {
                log.warn("⚠️ Unauthenticated attempt: command={}, dest={}",
                        accessor.getCommand(), accessor.getDestination());
                return null;
            }


            if (accessor.getDestination() != null && accessor.getDestination().contains("user.status")) {
                log.debug("📩 Passing message to destination: {}", accessor.getDestination());
            }
        }

        return message;
    }
}