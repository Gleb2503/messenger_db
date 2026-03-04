package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.NotificationType;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, columnDefinition = "varchar(30)")
    private NotificationType type;

    @Column(length = 200)
    private String title;

    @Column(length = 500)
    private String body;

    private Long entityId;

    @Column(length = 50)
    private String entityType;

    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}