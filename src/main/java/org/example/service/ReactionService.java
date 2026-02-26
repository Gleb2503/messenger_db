package org.example.service;

import org.example.entity.Reaction;
import org.example.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionService {

    private final ReactionRepository reactionRepository;

    public List<Reaction> getAllReactions() {
        return reactionRepository.findAll();
    }

    public List<Reaction> getReactionsByMessageId(Long messageId) {
        return reactionRepository.findByMessage_Id(messageId);
    }

    public List<Reaction> getReactionsByUserId(Long userId) {
        return reactionRepository.findByUser_Id(userId);
    }

    public Optional<Reaction> getReactionById(Long id) {
        return reactionRepository.findById(id);
    }

    @Transactional
    public Reaction addReaction(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    @Transactional
    public void removeReaction(Long id) {
        reactionRepository.deleteById(id);
    }

    @Transactional
    public void removeReactionByMessageAndUser(Long messageId, Long userId) {
        reactionRepository.deleteByMessageIdAndUserId(messageId, userId);
    }
}