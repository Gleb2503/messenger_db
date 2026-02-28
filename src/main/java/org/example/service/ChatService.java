package org.example.service;

import org.example.entity.Chat;
import org.example.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public Optional<Chat> getChatById(Long id) {
        return chatRepository.findById(id);
    }

    public List<Chat> getChatsByCreatedBy(Long userId) {
        return chatRepository.findByCreatedBy_Id(userId);
    }

    @Transactional
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    @Transactional
    public Chat updateChat(Long id, Chat chatDetails) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        chat.setName(chatDetails.getName());
        chat.setAvatarUrl(chatDetails.getAvatarUrl());
        return chatRepository.save(chat);
    }

    @Transactional
    public void deleteChat(Long id) {
        chatRepository.deleteById(id);
    }
}