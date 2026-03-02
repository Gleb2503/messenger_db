package org.example.dto.Message;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Запрос на создание сообщения"
)
public class CreateMessageRequest {
    @Schema(
            description = "ID чата",
            example = "1"
    )
    private Long chatId;
    @Schema(
            description = "ID отправителя",
            example = "1"
    )
    private Long senderId;
    @Schema(
            description = "Текст сообщения",
            example = "Привет всем!"
    )
    private String content;
    @Schema(
            description = "Тип сообщения",
            example = "text",
            allowableValues = {"text", "image", "video", "audio", "file"}
    )
    private String messageType;
    @Schema(
            description = "ID сообщения для ответа",
            example = "5"
    )
    private Long replyToId;

    public CreateMessageRequest() {
    }

    public Long getChatId() {
        return this.chatId;
    }

    public Long getSenderId() {
        return this.senderId;
    }

    public String getContent() {
        return this.content;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public Long getReplyToId() {
        return this.replyToId;
    }

    public void setChatId(final Long chatId) {
        this.chatId = chatId;
    }

    public void setSenderId(final Long senderId) {
        this.senderId = senderId;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setMessageType(final String messageType) {
        this.messageType = messageType;
    }

    public void setReplyToId(final Long replyToId) {
        this.replyToId = replyToId;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CreateMessageRequest)) {
            return false;
        } else {
            CreateMessageRequest other = (CreateMessageRequest)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$chatId = this.getChatId();
                    Object other$chatId = other.getChatId();
                    if (this$chatId == null) {
                        if (other$chatId == null) {
                            break label71;
                        }
                    } else if (this$chatId.equals(other$chatId)) {
                        break label71;
                    }

                    return false;
                }

                Object this$senderId = this.getSenderId();
                Object other$senderId = other.getSenderId();
                if (this$senderId == null) {
                    if (other$senderId != null) {
                        return false;
                    }
                } else if (!this$senderId.equals(other$senderId)) {
                    return false;
                }

                label57: {
                    Object this$replyToId = this.getReplyToId();
                    Object other$replyToId = other.getReplyToId();
                    if (this$replyToId == null) {
                        if (other$replyToId == null) {
                            break label57;
                        }
                    } else if (this$replyToId.equals(other$replyToId)) {
                        break label57;
                    }

                    return false;
                }

                Object this$content = this.getContent();
                Object other$content = other.getContent();
                if (this$content == null) {
                    if (other$content != null) {
                        return false;
                    }
                } else if (!this$content.equals(other$content)) {
                    return false;
                }

                Object this$messageType = this.getMessageType();
                Object other$messageType = other.getMessageType();
                if (this$messageType == null) {
                    if (other$messageType == null) {
                        return true;
                    }
                } else if (this$messageType.equals(other$messageType)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CreateMessageRequest;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $chatId = this.getChatId();
        result = result * 59 + ($chatId == null ? 43 : $chatId.hashCode());
        Object $senderId = this.getSenderId();
        result = result * 59 + ($senderId == null ? 43 : $senderId.hashCode());
        Object $replyToId = this.getReplyToId();
        result = result * 59 + ($replyToId == null ? 43 : $replyToId.hashCode());
        Object $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        Object $messageType = this.getMessageType();
        result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
        return result;
    }

    public String toString() {
        Long var10000 = this.getChatId();
        return "CreateMessageRequest(chatId=" + var10000 + ", senderId=" + this.getSenderId() + ", content=" + this.getContent() + ", messageType=" + this.getMessageType() + ", replyToId=" + this.getReplyToId() + ")";
    }
}