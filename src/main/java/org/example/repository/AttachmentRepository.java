package org.example.repository;

import org.example.entity.Attachment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {


    List<Attachment> findTop100ByOrderByCreatedAtDesc();


    List<Attachment> findTop100ByMessageIdOrderByCreatedAtDesc(Long messageId);


    List<Attachment> findTop100ByMessageChatIdOrderByCreatedAtDesc(Long chatId);


    @Query("SELECT a FROM Attachment a WHERE a.id IN " +
            "(SELECT MAX(a2.id) FROM Attachment a2 JOIN a2.message m WHERE m.chat IS NOT NULL GROUP BY m.chat.id)")
    List<Attachment> findLastAttachmentsPerChat();

    @Query("SELECT a FROM Attachment a JOIN a.message m WHERE m.chat.id = :chatId ORDER BY a.createdAt DESC")
    List<Attachment> findByChatIdOrderByCreatedAtDesc(@Param("chatId") Long chatId, Pageable pageable);

    void deleteByMessageId(Long messageId);


    boolean existsById(Long id);
    boolean existsByMessageId(Long messageId);
}