package org.example.service;

import org.example.dto.Chat.ChatResponse;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
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
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public List<ChatResponse> getLast100Chats() {
        return chatRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public ChatResponse getChatById(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + id));
        return convertToResponse(chat);
    }

    public List<ChatResponse> getLast100ChatsByCreator(Long createdById) {
        return chatRepository.findTop100ByCreatedBy_IdOrderByCreatedAtDesc(createdById)
                .stream()
                .map(this::convertToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public ChatResponse createChat(org.example.dto.Chat.CreateChatRequest request) {
        Chat chat = request.toEntity();
        if (chat.getCreatedBy() != null && chat.getCreatedBy().getId() != null) {
            User creator = userRepository.findById(chat.getCreatedBy().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Creator not found with id: " + chat.getCreatedBy().getId()));
            chat.setCreatedBy(creator);
        }
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        Chat saved = chatRepository.save(chat);
        return convertToResponse(saved);
    }

    @Transactional
    public ChatResponse updateChat(Long id, org.example.dto.Chat.CreateChatRequest request) {
        Chat existing = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + id));
        existing.setName(request.getName());
        existing.setType(request.getType() != null ? org.example.enums.ChatType.valueOf(request.getType()) : existing.getType());
        existing.setAvatarUrl(request.getAvatarUrl());
        existing.setUpdatedAt(LocalDateTime.now());
        Chat updated = chatRepository.save(existing);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteChat(Long id) {
        if (!chatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Chat not found with id: " + id);
        }
        chatRepository.deleteById(id);
    }

    private ChatResponse convertToResponse(Chat chat) {
        ChatResponse response = new ChatResponse();
        response.setId(chat.getId());
        response.setName(chat.getName());
        response.setType(chat.getType());
        response.setAvatarUrl(chat.getAvatarUrl());
        response.setCreatedAt(chat.getCreatedAt());
        response.setUpdatedAt(chat.getUpdatedAt());
        response.setLastMessageTime(chat.getLastMessageTime());

        if (chat.getCreatedBy() != null) {
            response.setCreatedBy(new org.example.dto.User.UserDTO(
                    chat.getCreatedBy().getId(),
                    chat.getCreatedBy().getUsername(),
                    chat.getCreatedBy().getEmail()
            ));
        }

        return response;
    }
}