package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "reply_to_id")
    private Message replyTo;

    @Column(name = "is_edited")
    private Boolean isEdited;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
    private List<Attachment> attachments;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
    private List<Reaction> reactions;

    public enum MessageType {
        text,
        image,
        video,
        audio,
        file,
        sticker,
        location,
        system
    }

    public enum DeliveryStatus {
        sent,
        delivered,
        read
    }
}