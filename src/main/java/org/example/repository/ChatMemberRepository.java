package org.example.repository;

import org.example.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    List<ChatMember> findByUser_Id(Long userId);

    List<ChatMember> findByChat_Id(Long chatId);

    Optional<ChatMember> findByChat_IdAndUser_Id(Long chatId, Long userId);
}