package org.example.repository;

import org.example.entity.MessageRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageReadsRepository extends JpaRepository<MessageRead, Long> {

    List<MessageRead> findTop100ByUserIdOrderByReadAtDesc(Long userId);

    List<MessageRead> findTop100ByMessageIdOrderByReadAtDesc(Long messageId);

    boolean existsByUserIdAndMessageId(Long userId, Long messageId);
}