package org.example.repository;

import org.example.entity.MessageRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageReadsRepository extends JpaRepository<MessageRead, Long> {

    List<MessageRead> findByMessage_Id(Long messageId);

    List<MessageRead> findByUser_Id(Long userId);

    Optional<MessageRead> findByMessage_IdAndUser_Id(Long messageId, Long userId);
}