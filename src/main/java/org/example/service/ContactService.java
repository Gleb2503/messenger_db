package org.example.service;

import org.example.entity.Contact;
import org.example.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.exeption.ResourceNotFoundException;

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

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public List<Contact> getContactsByUserId(Long userId) {
        return contactRepository.findByUser_Id(userId);
    }

    public List<Contact> getBlockedContacts(Long userId) {
        return contactRepository.findByUser_IdAndIsBlockedTrue(userId);
    }

    @Transactional
    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Transactional
    public Contact blockContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        contact.setIsBlocked(true);
        return contactRepository.save(contact);
    }

    @Transactional
    public Contact unblockContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        contact.setIsBlocked(false);
        return contactRepository.save(contact);
    }

    @Transactional
    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact not found with id: " + id);
        }
        contactRepository.deleteById(id);
    }
}