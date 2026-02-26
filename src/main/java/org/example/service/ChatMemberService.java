package org.example.service;

import org.example.entity.ChatMember;
import org.example.repository.ChatMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;

    public List<ChatMember> getAllMembers() {
        return chatMemberRepository.findAll();
    }

    public List<ChatMember> getMembersByChatId(Long chatId) {
        return chatMemberRepository.findByChat_Id(chatId);
    }

    public List<ChatMember> getMembersByUserId(Long userId) {
        return chatMemberRepository.findByUser_Id(userId);
    }

    public Optional<ChatMember> getMemberById(Long id) {
        return chatMemberRepository.findById(id);
    }

    public Optional<ChatMember> getMemberByChatAndUser(Long chatId, Long userId) {
        return chatMemberRepository.findByChat_IdAndUser_Id(chatId, userId);
    }

    @Transactional
    public ChatMember addMember(ChatMember chatMember) {
        return chatMemberRepository.save(chatMember);
    }

    @Transactional
    public ChatMember updateMemberRole(Long id, ChatMember.MemberRole role) {
        ChatMember member = chatMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setRole(role);
        return chatMemberRepository.save(member);
    }

    @Transactional
    public void removeMember(Long id) {
        chatMemberRepository.deleteById(id);
    }

    @Transactional
    public void banMember(Long chatId, Long userId) {
        ChatMember member = chatMemberRepository.findByChat_IdAndUser_Id(chatId, userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setIsActive(false);
        chatMemberRepository.save(member);
    }
}