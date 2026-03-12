package org.example.service;

import org.example.dto.ChatMember.ChatMemberResponse;
import org.example.dto.ChatMember.CreateChatMemberRequest;
import org.example.dto.Chat.ChatDTO;
import org.example.dto.User.UserDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public List<ChatMemberResponse> getLast100MembersByUser(Long userId) {
        return chatMemberRepository.findTop100ByUserIdAndIsActiveTrueOrderByJoinedAtDesc(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ChatMemberResponse> getLast100MembersByChat(Long chatId) {
        return chatMemberRepository.findTop100ByChatIdAndIsActiveTrueOrderByJoinedAtDesc(chatId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatMemberResponse addMember(CreateChatMemberRequest request) {
        ChatMember member = request.toEntity();

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

        ChatMember saved = chatMemberRepository.save(member);
        return convertToResponse(saved);
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
    public ChatMemberResponse updateRole(Long id, String role) {
        ChatMember member = chatMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChatMember not found"));
        member.setRole(MemberRole.valueOf(role));
        ChatMember updated = chatMemberRepository.save(member);
        return convertToResponse(updated);
    }

    private ChatMemberResponse convertToResponse(ChatMember member) {
        ChatMemberResponse response = new ChatMemberResponse();
        response.setId(member.getId());
        response.setRole(member.getRole());
        response.setIsActive(member.getIsActive());
        response.setJoinedAt(member.getJoinedAt());
        response.setLeftAt(member.getLeftAt());
        response.setIsMuted(member.getIsMuted());
        response.setIsPinned(member.getIsPinned());

        if (member.getChat() != null) {
            response.setChat(new ChatDTO(member.getChat().getId(), member.getChat().getName()));
        }

        if (member.getUser() != null) {
            response.setUser(new UserDTO(
                    member.getUser().getId(),
                    member.getUser().getUsername(),
                    member.getUser().getEmail()
            ));
        }

        return response;
    }
}