package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_status")
@NoArgsConstructor
public class UserStatus {

    @Id
    private Long userId;

    @Column(name = "is_online", nullable = false)
    private Boolean online = false;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen = LocalDateTime.now();

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public UserStatus(Long userId, Boolean online, LocalDateTime lastSeen) {
        this.userId = userId;
        this.online = online;
        this.lastSeen = lastSeen;
    }
}