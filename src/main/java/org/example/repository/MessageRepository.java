package org.example.repository;

import org.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    List<Message> findTop100ByChatIdOrderByCreatedAtDesc(Long chatId);

    List<Message> findTop100ByOrderByCreatedAtDesc();
    List<Message> findTop100BySenderIdOrderByCreatedAtDesc(Long senderId);


    List<Message> findBySenderId(Long senderId);
    boolean existsByChatId(Long chatId);
}