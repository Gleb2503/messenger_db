package org.example.repository;

import org.example.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findByMessage_Id(Long messageId);

    List<Reaction> findByUser_Id(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Reaction r WHERE r.message.id = :messageId AND r.user.id = :userId")
    void deleteByMessageIdAndUserId(@Param("messageId") Long messageId, @Param("userId") Long userId);
}