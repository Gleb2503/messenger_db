package org.example.service;

import org.example.dto.Contact.ContactResponse;
import org.example.dto.Contact.CreateContactRequest;
import org.example.dto.User.UserDTO;
import org.example.entity.Contact;
import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.ContactRepository;
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
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;


    public List<ContactResponse> getLast100Contacts() {
        return contactRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    public List<ContactResponse> getLast100ContactsByUser(Long userId) {
        return contactRepository.findTop100ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ContactResponse getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        return convertToResponse(contact);
    }

    @Transactional
    public ContactResponse addContact(CreateContactRequest request) {
        Contact contact = request.toEntity();

        if (contact.getUser() != null && contact.getUser().getId() != null) {
            User user = userRepository.findById(contact.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + contact.getUser().getId()));
            contact.setUser(user);
        }

        if (contact.getContactUser() != null && contact.getContactUser().getId() != null) {
            User contactUser = userRepository.findById(contact.getContactUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contact user not found with id: " + contact.getContactUser().getId()));
            contact.setContactUser(contactUser);
        }

        contact.setIsBlocked(false);
        contact.setCreatedAt(LocalDateTime.now());

        Contact saved = contactRepository.save(contact);
        return convertToResponse(saved);
    }

    @Transactional
    public ContactResponse blockContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        contact.setIsBlocked(true);
        Contact updated = contactRepository.save(contact);
        return convertToResponse(updated);
    }

    @Transactional
    public ContactResponse unblockContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        contact.setIsBlocked(false);
        Contact updated = contactRepository.save(contact);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact not found with id: " + id);
        }
        contactRepository.deleteById(id);
    }

    private ContactResponse convertToResponse(Contact contact) {
        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setNickname(contact.getNickname());
        response.setIsBlocked(contact.getIsBlocked());
        response.setCreatedAt(contact.getCreatedAt());

        if (contact.getUser() != null) {
            response.setUser(new UserDTO(
                    contact.getUser().getId(),
                    contact.getUser().getUsername(),
                    contact.getUser().getEmail()
            ));
        }

        if (contact.getContactUser() != null) {
            response.setContactUser(new UserDTO(
                    contact.getContactUser().getId(),
                    contact.getContactUser().getUsername(),
                    contact.getContactUser().getEmail()
            ));
        }

        return response;
    }
}