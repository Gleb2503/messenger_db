package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("version", "1.0.0");
        response.put("service", "messenger-backend");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> dbHealth() {
        Map<String, Object> response = new HashMap<>();
        try {
            jdbcTemplate.execute("SELECT 1");
            response.put("status", "UP");
            response.put("database", "PostgreSQL");
            response.put("timestamp", LocalDateTime.now().toString());
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("error", e.getMessage());
            response.put("timestamp", LocalDateTime.now().toString());
            return ResponseEntity.status(503).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ready")
    public ResponseEntity<Map<String, Object>> readiness() {
        Map<String, Object> response = new HashMap<>();
        response.put("ready", true);
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}