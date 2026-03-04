package org.example.repository;

import org.example.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByMessageId(Long messageId);

    List<Attachment> findByMessageChatId(Long chatId);

    @Query("SELECT a FROM Attachment a WHERE a.id IN (SELECT MAX(a2.id) FROM Attachment a2 JOIN a2.message m GROUP BY m.chat.id)")
    List<Attachment> findLastAttachmentsPerChat();

    @Query("SELECT a FROM Attachment a WHERE a.message.chat.id = :chatId ORDER BY a.createdAt DESC")
    List<Attachment> findByChatIdOrderByCreatedAtDesc(@Param("chatId") Long chatId, org.springframework.data.domain.Pageable pageable);
}