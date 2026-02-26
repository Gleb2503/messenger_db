package org.example.repository;

import org.example.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByChat_IdOrderByCreatedAtDesc(Long chatId, Pageable pageable);

    List<Message> findBySender_Id(Long senderId);
}