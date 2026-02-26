package org.example.repository;

import org.example.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByUser_Id(Long userId);

    List<Contact> findByUser_IdAndIsBlocked(Long userId, Boolean isBlocked);

    List<Contact> findByUser_IdAndIsBlockedTrue(Long userId);

    Optional<Contact> findByUser_IdAndContactUser_Id(Long userId, Long contactUserId);
}