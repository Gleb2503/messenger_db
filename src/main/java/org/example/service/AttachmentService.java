package org.example.service;

import org.example.entity.Attachment;
import org.example.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAll();
    }

    public List<Attachment> getAttachmentsByMessageId(Long messageId) {
        return attachmentRepository.findByMessage_Id(messageId);
    }

    public Optional<Attachment> getAttachmentById(Long id) {
        return attachmentRepository.findById(id);
    }

    @Transactional
    public Attachment uploadAttachment(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    @Transactional
    public void deleteAttachment(Long id) {
        attachmentRepository.deleteById(id);
    }

    @Transactional
    public void deleteAttachmentsByMessageId(Long messageId) {
        attachmentRepository.deleteByMessageId(messageId);
    }
}