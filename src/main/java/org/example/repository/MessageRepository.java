package org.example.repository;

import org.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findTop100ByChatIdOrderByCreatedAtDesc(Long chatId);

    List<Message> findTop100BySenderIdOrderByCreatedAtDesc(Long senderId);

    List<Message> findTop100ByOrderByCreatedAtDesc();

    Optional<Message> findTopByChatIdOrderByCreatedAtDesc(Long chatId);

    @Query("SELECT m FROM Message m WHERE m.chat.id IN :chatIds ORDER BY m.createdAt DESC")
    List<Message> findLastMessagesByChatIds(@Param("chatIds") List<Long> chatIds);
}