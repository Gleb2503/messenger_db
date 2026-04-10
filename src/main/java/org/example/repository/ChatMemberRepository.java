package org.example.repository;

import org.example.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    boolean existsByChatIdAndUserUsername(Long chatId, String username);

    Optional<ChatMember> findByChatIdAndUserUsername(Long chatId, String username);

    void deleteByChatIdAndUserUsername(Long chatId, String username);

    long countByChatId(Long chatId);

    void deleteByChatId(Long chatId);



    List<ChatMember> findTop100ByChatIdAndIsActiveTrueOrderByJoinedAtDesc(Long chatId);

    List<ChatMember> findByChatIdAndIsActiveTrue(Long chatId);

    List<ChatMember> findTop100ByUserIdAndIsActiveTrueOrderByJoinedAtDesc(Long chatId);

    List<ChatMember> findByChatIdOrderByJoinedAtAsc(Long chatId);

    boolean existsByChatIdAndUserId(Long chatId, Long userId);

    Optional<ChatMember> findByChatIdAndUserId(Long chatId, Long userId);
}