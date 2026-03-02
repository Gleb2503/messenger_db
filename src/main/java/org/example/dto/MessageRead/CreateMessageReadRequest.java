package org.example.dto.MessageRead;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Запрос на отметку прочтения сообщения"
)
public class CreateMessageReadRequest {
    @Schema(
            description = "ID сообщения",
            example = "1"
    )
    private Long messageId;
    @Schema(
            description = "ID пользователя",
            example = "2"
    )
    private Long userId;

    public CreateMessageReadRequest() {
    }

    public Long getMessageId() {
        return this.messageId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setMessageId(final Long messageId) {
        this.messageId = messageId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CreateMessageReadRequest)) {
            return false;
        } else {
            CreateMessageReadRequest other = (CreateMessageReadRequest)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$messageId = this.getMessageId();
                Object other$messageId = other.getMessageId();
                if (this$messageId == null) {
                    if (other$messageId != null) {
                        return false;
                    }
                } else if (!this$messageId.equals(other$messageId)) {
                    return false;
                }

                Object this$userId = this.getUserId();
                Object other$userId = other.getUserId();
                if (this$userId == null) {
                    if (other$userId != null) {
                        return false;
                    }
                } else if (!this$userId.equals(other$userId)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CreateMessageReadRequest;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $messageId = this.getMessageId();
        result = result * 59 + ($messageId == null ? 43 : $messageId.hashCode());
        Object $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        return result;
    }

    public String toString() {
        Long var10000 = this.getMessageId();
        return "CreateMessageReadRequest(messageId=" + var10000 + ", userId=" + this.getUserId() + ")";
    }
}
