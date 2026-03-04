package org.example.service;

import org.example.entity.ChatMember;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.enums.MemberRole;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.ChatMemberRepository;
import org.example.repository.ChatRepository;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public List<ChatMember> getLast100MembersByUser(Long userId) {
        return chatMemberRepository.findTop100ByUserIdAndIsActiveTrueOrderByJoinedAtDesc(userId);
    }

    public List<ChatMember> getLast100MembersByChat(Long chatId) {
        return chatMemberRepository.findTop100ByChatIdAndIsActiveTrueOrderByJoinedAtDesc(chatId);
    }

    @Transactional
    public ChatMember addMember(ChatMember member) {
        if (member.getChat() != null && member.getChat().getId() != null) {
            Chat chat = chatRepository.findById(member.getChat().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
            member.setChat(chat);
        }

        if (member.getUser() != null && member.getUser().getId() != null) {
            User user = userRepository.findById(member.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            member.setUser(user);
        }

        if (chatMemberRepository.existsByChatIdAndUserId(member.getChat().getId(), member.getUser().getId())) {
            throw new RuntimeException("User is already a member of this chat");
        }

        member.setRole(member.getRole() != null ? member.getRole() : MemberRole.member);
        member.setIsActive(true);
        member.setJoinedAt(LocalDateTime.now());

        return chatMemberRepository.save(member);
    }

    @Transactional
    public void removeMember(Long id) {
        ChatMember member = chatMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChatMember not found"));
        member.setIsActive(false);
        member.setLeftAt(LocalDateTime.now());
        chatMemberRepository.save(member);
    }

    @Transactional
    public ChatMember updateRole(Long id, String role) {
        ChatMember member = chatMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChatMember not found"));
        member.setRole(MemberRole.valueOf(role));
        return chatMemberRepository.save(member);
    }
}