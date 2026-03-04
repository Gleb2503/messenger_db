package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.MemberRole;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_members")
public class ChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    @JsonIgnore
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, columnDefinition = "varchar(20)")
    private MemberRole role;

    private Boolean isActive;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;

    @Column(nullable = false)
    private Boolean isMuted;

    @Column(nullable = false)
    private Boolean isPinned;
}