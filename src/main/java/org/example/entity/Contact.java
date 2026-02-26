package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "contact_user_id", nullable = false)
    private User contactUser;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}