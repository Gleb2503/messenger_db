package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/health/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/chats/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/chats/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/chats/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/chats/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/messages/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/messages/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/messages/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/messages/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/notifications/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/contacts/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/contacts/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/contacts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/contacts/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/reactions/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/reactions/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/reactions/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/attachments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/attachments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/attachments/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/chat-members/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/chat-members/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/chat-members/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/chat-members/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/sessions/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/sessions/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/sessions/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/sessions/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/message-reads/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/message-reads/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/message-reads/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/api-keys/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/api-keys/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}