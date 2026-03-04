package org.example.repository;

import org.example.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findTop100ByMessageIdOrderByCreatedAtDesc(Long messageId);

    List<Reaction> findTop100ByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByMessageIdAndUserId(Long messageId, Long userId);

    void deleteByMessageIdAndUserId(Long messageId, Long userId);
}