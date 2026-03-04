package org.example.repository;

import org.example.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    List<ChatMember> findTop100ByUserIdAndIsActiveTrueOrderByJoinedAtDesc(Long userId);

    List<ChatMember> findTop100ByChatIdAndIsActiveTrueOrderByJoinedAtDesc(Long chatId);

    boolean existsByChatIdAndUserId(Long chatId, Long userId);
}