package org.example.service;

import org.example.dto.Attachment.AttachmentResponse;
import org.example.dto.Attachment.CreateAttachmentRequest;
import org.example.entity.Attachment;
import org.example.entity.Message;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.AttachmentRepository;
import org.example.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final MessageRepository messageRepository;
    private final FileStorageService fileStorageService;

    public List<AttachmentResponse> getLast100Attachments() {
        return attachmentRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public AttachmentResponse getAttachmentById(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with id: " + id));
        return convertToResponse(attachment);
    }

    public List<AttachmentResponse> getLast100AttachmentsByMessage(Long messageId) {
        return attachmentRepository.findTop100ByMessageIdOrderByCreatedAtDesc(messageId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<AttachmentResponse> getLast100AttachmentsByChat(Long chatId) {
        return attachmentRepository.findTop100ByMessageChatIdOrderByCreatedAtDesc(chatId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<AttachmentResponse> getLastAttachmentsPerChat() {
        return attachmentRepository.findLastAttachmentsPerChat()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AttachmentResponse createAttachment(CreateAttachmentRequest request, MultipartFile file, Long userId) {
        log.info("Creating attachment: fileName={}, fileSize={}", request.getFileName(), request.getFileSize());

        // 1. Загружаем файл в облако
        String fileUrl;
        try {
            fileUrl = fileStorageService.uploadFile(file, userId.toString());
        } catch (Exception e) {
            log.error("Failed to upload file to storage", e);
            throw new RuntimeException("Failed to upload file to cloud storage", e);
        }


        Attachment attachment = request.toEntity();
        attachment.setFileUrl(fileUrl.trim());

        if (attachment.getMessage() != null && attachment.getMessage().getId() != null) {
            Message message = messageRepository.findById(attachment.getMessage().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + attachment.getMessage().getId()));
            attachment.setMessage(message);
        }

        attachment.setCreatedAt(LocalDateTime.now());

        Attachment saved = attachmentRepository.save(attachment);
        log.info("Attachment saved: id={}", saved.getId());

        return convertToResponse(saved);
    }

    @Transactional
    public void deleteAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with id: " + id));


        fileStorageService.deleteFile(attachment.getFileUrl());


        attachmentRepository.deleteById(id);
        log.info("Attachment deleted: id={}", id);
    }

    private AttachmentResponse convertToResponse(Attachment attachment) {
        AttachmentResponse response = new AttachmentResponse();
        response.setId(attachment.getId());
        response.setFileUrl(attachment.getFileUrl().trim()); // ✅ Убираем пробелы!
        response.setFileName(attachment.getFileName());
        response.setFileSize(attachment.getFileSize());
        response.setFileType(attachment.getFileType());
        response.setThumbnailUrl(attachment.getThumbnailUrl());
        response.setCreatedAt(attachment.getCreatedAt());

        if (attachment.getMessage() != null) {
            response.setMessage(convertMessageToResponse(attachment.getMessage()));
        }

        return response;
    }

    private org.example.dto.Message.MessageResponse convertMessageToResponse(Message message) {
        if (message == null) return null;

        org.example.dto.Message.MessageResponse response = new org.example.dto.Message.MessageResponse();
        response.setId(message.getId());
        response.setContent(message.getContent());
        response.setMessageType(message.getMessageType());
        response.setIsEdited(message.getIsEdited() != null ? message.getIsEdited() : false);
        response.setIsDeleted(message.getIsDeleted() != null ? message.getIsDeleted() : false);
        response.setDeliveryStatus(message.getDeliveryStatus());
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());

        if (message.getSender() != null) {
            var sender = new org.example.dto.User.UserDTO();
            sender.setId(message.getSender().getId());
            sender.setUsername(message.getSender().getUsername());
            sender.setEmail(message.getSender().getEmail());
            response.setSender(sender);
        }

        if (message.getChat() != null) {
            var chat = new org.example.dto.Chat.ChatDTO();
            chat.setId(message.getChat().getId());
            chat.setName(message.getChat().getName());
            response.setChat(chat);
        }

        return response;
    }
}