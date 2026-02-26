package org.example.repository;

import org.example.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByCreatedBy_Id(Long userId);

    @Query("SELECT c FROM Chat c JOIN c.members m WHERE m.user.id = :userId")
    List<Chat> findByMemberUserId(@Param("userId") Long userId);
}