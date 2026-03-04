package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.MessageType;
import org.example.enums.DeliveryStatus;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    @JsonIgnore
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    private User sender;

    @Column(length = 5000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, columnDefinition = "varchar(20)")
    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_id")
    @JsonIgnore
    private Message replyTo;

    private Boolean isEdited;
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, columnDefinition = "varchar(20)")
    private DeliveryStatus deliveryStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments;

    @JsonIgnore
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions;
}