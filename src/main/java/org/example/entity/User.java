package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "phone_number", unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "status", length = 20)
    private String status = "offline";

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Message> messages;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatMember> chatMembers;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Contact> contacts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserSettings> userSettings;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Session> sessions;
}