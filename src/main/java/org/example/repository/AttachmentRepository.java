package org.example.repository;

import org.example.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByMessage_Id(Long messageId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Attachment a WHERE a.message.id = :messageId")
    void deleteByMessageId(@Param("messageId") Long messageId);
}