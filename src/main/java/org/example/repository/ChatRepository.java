package org.example.repository;

import org.example.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.id IN :chatIds ORDER BY COALESCE(c.lastMessageTime, c.updatedAt, c.createdAt) DESC")
    List<Chat> findChatsSortedByLastMessage(@Param("chatIds") List<Long> chatIds);

    @Query("SELECT c FROM Chat c JOIN ChatMember cm ON c.id = cm.chat.id WHERE cm.user.id = :userId ORDER BY COALESCE(c.lastMessageTime, c.updatedAt, c.createdAt) DESC")
    List<Chat> findChatsByUserIdSorted(@Param("userId") Long userId);

    List<Chat> findTop100ByOrderByCreatedAtDesc();

    List<Chat> findTop100ByCreatedBy_IdOrderByCreatedAtDesc(Long createdById);

    List<Chat> findTop100ByTypeOrderByCreatedAtDesc(String type);

    boolean existsByNameAndCreatedBy_Id(String name, Long createdById);

    @Query("SELECT c FROM Chat c JOIN ChatMember m1 ON c.id = m1.chat.id JOIN ChatMember m2 ON c.id = m2.chat.id WHERE m1.user.id = :user1 AND m2.user.id = :user2 AND c.type = 'private_chat'")
    Optional<Chat> findExistingPrivateChat(@Param("user1") Long user1, @Param("user2") Long user2);
}