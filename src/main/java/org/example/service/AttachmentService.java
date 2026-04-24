package org.example.service;

import org.example.dto.Attachment.AttachmentResponse;
import org.example.dto.Attachment.CreateAttachmentRequest;
import org.example.entity.Attachment;
import org.example.entity.Message;
import org.example.enums.MessageType;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.AttachmentRepository;
import org.example.repository.ChatMemberRepository;
import org.example.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
    private final ChatMemberRepository chatMemberRepository;
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

    public Attachment getAttachmentEntityById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found: " + id));
    }

    public boolean canUserDownload(String username, Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found: " + attachmentId));
        return canUserDownload(username, attachment);
    }

    public boolean canUserDownload(String username, Attachment attachment) {
        if (attachment == null || attachment.getMessage() == null) {
            return false;
        }

        Message message = attachment.getMessage();

        if (message.getSender() != null &&
                message.getSender().getUsername() != null &&
                message.getSender().getUsername().equals(username)) {
            return true;
        }

        if (message.getChat() != null && message.getChat().getId() != null) {
            boolean isMember = chatMemberRepository.existsByChatIdAndUserUsername(
                    message.getChat().getId(),
                    username
            );
            if (isMember) {
                return true;
            }
        }

        return false;
    }

    @Transactional
    public AttachmentResponse createAttachment(CreateAttachmentRequest request, MultipartFile file, Long userId) {
        log.info("Creating attachment: fileName={}, fileSize={}, fileType={}",
                request.getFileName(), request.getFileSize(), request.getFileType());

        String fileUrl;
        try {
            fileUrl = fileStorageService.uploadFile(file, userId.toString());
        } catch (Exception e) {
            log.error("Failed to upload file to storage", e);
            throw new RuntimeException("Failed to upload file to cloud storage", e);
        }

        Attachment attachment = request.toEntity();
        attachment.setFileUrl(fileUrl.trim());

        if (request.getMessageId() != null) {
            Message message = messageRepository.findById(request.getMessageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Message not found: " + request.getMessageId()));

            attachment.setMessage(message);


            String fileType = attachment.getFileType();
            if (fileType != null && !fileType.isEmpty()) {
                if (fileType.startsWith("image/")) {
                    message.setMessageType(MessageType.image);
                } else if (fileType.startsWith("video/")) {
                    message.setMessageType(MessageType.video);
                } else {
                    message.setMessageType(MessageType.file);
                }
                messageRepository.save(message);
                log.debug("Updated message {} type to {}", message.getId(), message.getMessageType());
            }
        }

        attachment.setCreatedAt(LocalDateTime.now());
        Attachment saved = attachmentRepository.save(attachment);

        return convertToResponse(saved);
    }

    public Resource downloadAttachmentWithAccessCheck(Long attachmentId, String username) {
        if (!canUserDownload(username, attachmentId)) {
            throw new AccessDeniedException("Access denied to attachment: " + attachmentId);
        }

        Attachment attachment = getAttachmentEntityById(attachmentId);

        byte[] fileBytes;
        try (InputStream inputStream = fileStorageService.getFileStream(attachment.getFileUrl())) {
            fileBytes = inputStream.readAllBytes();
        } catch (IOException e) {
            log.error("Failed to read file from storage: {}", attachment.getFileUrl(), e);
            throw new RuntimeException("Failed to read file", e);
        }

        String contentType = attachment.getFileType();
        if (contentType == null || contentType.isEmpty()) {
            contentType = fileStorageService.getFileContentType(attachment.getFileUrl());
        }

        return new ByteArrayFileResource(fileBytes, contentType, attachment.getFileName());
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
        response.setFileUrl(attachment.getFileUrl().trim());
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

    private static class ByteArrayFileResource extends ByteArrayResource {
        private final String contentType;
        private final String filename;

        public ByteArrayFileResource(byte[] byteArray, String contentType, String filename) {
            super(byteArray);
            this.contentType = contentType != null ? contentType : "application/octet-stream";
            this.filename = filename;
        }

        public String getContentType() {
            return contentType;
        }

        @Override
        public String getFilename() {
            return filename;
        }

        @Override
        public String getDescription() {
            return "Attachment: " + filename;
        }
    }
}