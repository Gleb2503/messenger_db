package org.example.repository;

import org.example.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {


    List<Contact> findTop100ByUserIdOrderByCreatedAtDesc(Long userId);

    List<Contact> findTop100ByOrderByCreatedAtDesc();

    List<Contact> findByUserIdAndIsBlockedFalse(Long userId);

    boolean existsByUserIdAndContactUserId(Long userId, Long contactUserId);
}