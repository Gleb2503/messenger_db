package org.example.service;

import org.example.entity.Chat;
import org.example.entity.ChatMember;
import org.example.entity.User;
import org.example.repository.ChatRepository;
import org.example.repository.ChatMemberRepository;
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
    private final ChatMemberRepository chatMemberRepository;

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public Optional<Chat> getChatById(Long id) {
        return chatRepository.findById(id);
    }

    public List<Chat> getChatsByUserId(Long userId) {
        return chatRepository.findByMemberUserId(userId);
    }

    @Transactional
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    @Transactional
    public Chat createPrivateChat(User user1, User user2) {
        Chat chat = new Chat();
        chat.setType(Chat.ChatType.private_chat);
        chat.setCreatedBy(user1);
        Chat savedChat = chatRepository.save(chat);

        ChatMember member1 = new ChatMember();
        member1.setChat(savedChat);
        member1.setUser(user1);
        member1.setRole(ChatMember.MemberRole.owner);
        member1.setIsActive(true);
        chatMemberRepository.save(member1);

        ChatMember member2 = new ChatMember();
        member2.setChat(savedChat);
        member2.setUser(user2);
        member2.setRole(ChatMember.MemberRole.member);
        member2.setIsActive(true);
        chatMemberRepository.save(member2);

        return savedChat;
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