package org.example.service;

import org.example.entity.Contact;
import org.example.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactService {

    private final ContactRepository contactRepository;

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public List<Contact> getContactsByUserId(Long userId) {
        return contactRepository.findByUser_Id(userId);
    }

    public List<Contact> getBlockedContacts(Long userId) {
        return contactRepository.findByUser_IdAndIsBlocked(userId, true);
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public Optional<Contact> getContactByUserAndContactUser(Long userId, Long contactUserId) {
        return contactRepository.findByUser_IdAndContactUser_Id(userId, contactUserId);
    }

    @Transactional
    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Transactional
    public Contact updateContactNickname(Long id, String nickname) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setNickname(nickname);
        return contactRepository.save(contact);
    }

    @Transactional
    public Contact blockContact(Long userId, Long contactUserId) {
        return contactRepository.findByUser_IdAndContactUser_Id(userId, contactUserId)
                .map(contact -> {
                    contact.setIsBlocked(true);
                    return contactRepository.save(contact);
                })
                .orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    @Transactional
    public Contact unblockContact(Long userId, Long contactUserId) {
        return contactRepository.findByUser_IdAndContactUser_Id(userId, contactUserId)
                .map(contact -> {
                    contact.setIsBlocked(false);
                    return contactRepository.save(contact);
                })
                .orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    @Transactional
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}