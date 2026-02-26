package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "push_enabled")
    private Boolean pushEnabled;

    @Column(name = "email_enabled")
    private Boolean emailEnabled;

    @Column(name = "sound_enabled")
    private Boolean soundEnabled;

    @Column(name = "theme", length = 20)
    private String theme;

    @Column(name = "language", length = 10)
    private String language;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}