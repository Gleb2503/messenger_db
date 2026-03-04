package org.example.service;

import org.example.entity.Attachment;
import org.example.entity.Message;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.AttachmentRepository;
import org.example.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final MessageRepository messageRepository;

    public List<Attachment> getRecentAttachmentsByChatId(Long chatId, int limit) {
        if (limit > 500) {
            limit = 500;
        }
        var pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return attachmentRepository.findByChatIdOrderByCreatedAtDesc(chatId, pageable);
    }

    public List<Attachment> getLastAttachmentsPerChat() {
        return attachmentRepository.findLastAttachmentsPerChat();
    }

    public List<Attachment> getAttachmentsByMessageId(Long messageId) {
        return attachmentRepository.findByMessageId(messageId);
    }

    public Attachment getAttachmentById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with id: " + id));
    }

    @Transactional
    public Attachment uploadAttachment(Attachment attachment) {
        if (attachment.getMessage() != null && attachment.getMessage().getId() != null) {
            Message message = messageRepository.findById(attachment.getMessage().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + attachment.getMessage().getId()));
            attachment.setMessage(message);
        }
        attachment.setCreatedAt(java.time.LocalDateTime.now());
        return attachmentRepository.save(attachment);
    }

    @Transactional
    public void deleteAttachment(Long id) {
        if (!attachmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Attachment not found with id: " + id);
        }
        attachmentRepository.deleteById(id);
    }
}