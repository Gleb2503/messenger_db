package org.example.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }


        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorization = accessor.getFirstNativeHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                log.warn("❌ WebSocket CONNECT without Authorization header");
                return null;
            }

            String token = authorization.substring(7);

            try {
                if (!jwtTokenProvider.validateToken(token)) {
                    log.warn("❌ WebSocket CONNECT with invalid token");
                    return null;
                }

                Long userId = jwtTokenProvider.getUserIdFromToken(token);
                String username = jwtTokenProvider.getUsernameFromToken(token);


                if (accessor.getSessionAttributes() == null) {
                    accessor.setSessionAttributes(new java.util.HashMap<>());
                }
                accessor.getSessionAttributes().put("userId", userId);
                accessor.getSessionAttributes().put("username", username);


                accessor.setUser(() -> userId.toString());

                log.info("✅ WebSocket authenticated: userId={}, username={}", userId, username);

            } catch (Exception e) {
                log.error("❌ WebSocket authentication error: {}", e.getMessage(), e);
                return null;
            }
        }

        else {
            Long userId = (Long) accessor.getSessionAttributes().get("userId");

            if (userId == null) {
                log.warn("⚠️ Unauthenticated command: command={}, dest={}, sessionAttrs={}",
                        accessor.getCommand(),
                        accessor.getDestination(),
                        accessor.getSessionAttributes());
                return null;
            }
        }

        return message;
    }
}