package org.example.repository;

import org.example.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatIdOrderByCreatedAtDesc(Long chatId, Pageable pageable);

    List<Message> findByChatIdOrderByCreatedAtAsc(Long chatId);

    List<Message> findBySender_Id(Long senderId);

    @Query("SELECT m FROM Message m WHERE m.id IN (SELECT MAX(m2.id) FROM Message m2 GROUP BY m2.chat.id)")
    List<Message> findLastMessagePerChat();

    @Query("SELECT m FROM Message m WHERE m.sender.id = :senderId ORDER BY m.createdAt DESC")
    List<Message> findBySenderId(@Param("senderId") Long senderId);
}