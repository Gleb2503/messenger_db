package org.example.service;

import org.example.dto.Message.MessageDTO;
import org.example.dto.Reaction.ReactionResponse;
import org.example.dto.Reaction.CreateReactionRequest;
import org.example.dto.User.UserDTO;
import org.example.entity.Reaction;
import org.example.entity.Message;
import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.ReactionRepository;
import org.example.repository.MessageRepository;
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
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<ReactionResponse> getLast100ReactionsByMessage(Long messageId) {
        return reactionRepository.findTop100ByMessageIdOrderByCreatedAtDesc(messageId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ReactionResponse> getLast100ReactionsByUser(Long userId) {
        return reactionRepository.findTop100ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReactionResponse addReaction(CreateReactionRequest request) {
        Reaction reaction = request.toEntity();

        if (reaction.getMessage() != null && reaction.getMessage().getId() != null) {
            Message message = messageRepository.findById(reaction.getMessage().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + reaction.getMessage().getId()));
            reaction.setMessage(message);
        }

        if (reaction.getUser() != null && reaction.getUser().getId() != null) {
            User user = userRepository.findById(reaction.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + reaction.getUser().getId()));
            reaction.setUser(user);
        }

        reaction.setCreatedAt(LocalDateTime.now());
        Reaction saved = reactionRepository.save(reaction);
        return convertToResponse(saved);
    }

    @Transactional
    public void removeReaction(Long messageId, Long userId) {
        reactionRepository.deleteByMessageIdAndUserId(messageId, userId);
    }

    @Transactional
    public void deleteReaction(Long id) {
        if (!reactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reaction not found with id: " + id);
        }
        reactionRepository.deleteById(id);
    }

    private ReactionResponse convertToResponse(Reaction reaction) {
        ReactionResponse response = new ReactionResponse();
        response.setId(reaction.getId());
        response.setEmoji(reaction.getEmoji());
        response.setCreatedAt(reaction.getCreatedAt());

        if (reaction.getMessage() != null) {
            response.setMessage(new MessageDTO(
                    reaction.getMessage().getId(),
                    reaction.getMessage().getContent()
            ));
        }

        if (reaction.getUser() != null) {
            response.setUser(new UserDTO(
                    reaction.getUser().getId(),
                    reaction.getUser().getUsername(),
                    reaction.getUser().getEmail()
            ));
        }

        return response;
    }
}