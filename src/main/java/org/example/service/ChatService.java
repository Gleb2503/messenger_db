package org.example.service;

import org.example.dto.Chat.ChatResponse;
import org.example.dto.Chat.CreateChatRequest;
import org.example.dto.User.UserDTO;
import org.example.entity.Chat;
import org.example.entity.ChatMember;
import org.example.entity.User;
import org.example.enums.ChatType;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.ChatRepository;
import org.example.repository.ChatMemberRepository;
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
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMemberRepository memberRepository;

    public List<ChatResponse> getUserChats(Long userId) {
        List<ChatMember> activeMembers = memberRepository.findTop100ByUserIdAndIsActiveTrueOrderByJoinedAtDesc(userId);

        return activeMembers.stream()
                .map(member -> convertToResponse(member.getChat(), userId))
                .collect(Collectors.toList());
    }

    public List<ChatResponse> getLast100Chats() {
        return chatRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .map(chat -> convertToResponse(chat, null))
                .collect(Collectors.toList());
    }

    public ChatResponse getChatById(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + id));
        return convertToResponse(chat, null);
    }

    public List<ChatResponse> getLast100ChatsByCreator(Long createdById) {
        return chatRepository.findTop100ByCreatedBy_IdOrderByCreatedAtDesc(createdById)
                .stream()
                .map(chat -> convertToResponse(chat, null))
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatResponse createChat(CreateChatRequest request, Long currentUserId) {
        ChatType type = ChatType.valueOf(request.getType());

        if (type == ChatType.private_chat) {
            return createPrivateChat(request.getParticipantPhone(), currentUserId);
        } else if (type == ChatType.group || type == ChatType.channel) {
            return createGroupChat(request.getName(), currentUserId, type);
        } else {
            throw new IllegalArgumentException("Неподдерживаемый тип чата: " + type);
        }
    }

    private ChatResponse createPrivateChat(String participantPhone, Long currentUserId) {
        if (participantPhone == null || participantPhone.trim().isEmpty()) {
            throw new IllegalArgumentException("Для личного чата необходимо указать participantPhone");
        }

        User targetUser = userRepository.findByPhoneNumber(participantPhone)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Пользователь с телефоном " + participantPhone + " не найден"));

        if (targetUser.getId().equals(currentUserId)) {
            throw new IllegalArgumentException("Нельзя создать чат с самим собой");
        }

        var existingChatOpt = chatRepository.findExistingPrivateChat(currentUserId, targetUser.getId());
        if (existingChatOpt.isPresent()) {
            return convertToResponse(existingChatOpt.get(), currentUserId);
        }

        Chat chat = new Chat();
        chat.setType(ChatType.private_chat);
        chat.setName(targetUser.getUsername());
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        chat.setPinned(false);

        User creator = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Creator not found"));
        chat.setCreatedBy(creator);

        Chat savedChat = chatRepository.save(chat);

        addMember(savedChat, creator);
        addMember(savedChat, targetUser);

        return convertToResponse(savedChat, currentUserId);
    }

    private ChatResponse createGroupChat(String groupName, Long currentUserId, ChatType type) {
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Для группового чата необходимо указать name");
        }

        Chat chat = new Chat();
        chat.setType(type);
        chat.setName(groupName);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        chat.setPinned(false);

        User creator = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Creator not found"));
        chat.setCreatedBy(creator);

        Chat savedChat = chatRepository.save(chat);

        addMember(savedChat, creator);

        return convertToResponse(savedChat, currentUserId);
    }

    private void addMember(Chat chat, User user) {
        ChatMember member = new ChatMember();
        member.setChat(chat);
        member.setUser(user);
        member.setJoinedAt(LocalDateTime.now());
        member.setIsActive(true);
        member.setIsMuted(false);
        member.setIsPinned(false);
        memberRepository.save(member);
    }

    @Transactional
    public ChatResponse updateChat(Long id, CreateChatRequest request) {
        Chat existing = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + id));

        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        if (request.getType() != null) {
            existing.setType(ChatType.valueOf(request.getType()));
        }
        if (request.getAvatarUrl() != null) {
            existing.setAvatarUrl(request.getAvatarUrl());
        }
        existing.setUpdatedAt(LocalDateTime.now());

        Chat updated = chatRepository.save(existing);
        return convertToResponse(updated, null);
    }

    @Transactional
    public ChatResponse togglePin(Long id, Boolean pinned) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + id));
        chat.setPinned(pinned);
        chat.setUpdatedAt(LocalDateTime.now());
        Chat updated = chatRepository.save(chat);
        return convertToResponse(updated, null);
    }

    @Transactional
    public void deleteChat(Long id) {
        if (!chatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Chat not found with id: " + id);
        }
        memberRepository.deleteByChatId(id);
        chatRepository.deleteById(id);
    }

    private ChatResponse convertToResponse(Chat chat, Long currentUserId) {
        ChatResponse response = new ChatResponse();
        response.setId(chat.getId());
        response.setType(chat.getType());
        response.setAvatarUrl(chat.getAvatarUrl());
        response.setCreatedAt(chat.getCreatedAt());
        response.setUpdatedAt(chat.getUpdatedAt());
        response.setLastMessageTime(chat.getLastMessageTime());
        response.setPinned(chat.isPinned());


        if (ChatType.private_chat.equals(chat.getType()) && currentUserId != null) {
            String partnerName = findPartnerName(chat.getId(), currentUserId);
            response.setName(partnerName != null ? partnerName : chat.getName());
        } else {
            response.setName(chat.getName());
        }

        if (chat.getCreatedBy() != null) {
            response.setCreatedBy(new UserDTO(
                    chat.getCreatedBy().getId(),
                    chat.getCreatedBy().getUsername(),
                    chat.getCreatedBy().getEmail()
            ));
        }

        return response;
    }


    private String findPartnerName(Long chatId, Long currentUserId) {
        List<ChatMember> members = memberRepository.findByChatIdAndIsActiveTrue(chatId);

        for (ChatMember member : members) {
            if (!member.getUser().getId().equals(currentUserId)) {
                return member.getUser().getUsername();
            }
        }
        return null;
    }
}