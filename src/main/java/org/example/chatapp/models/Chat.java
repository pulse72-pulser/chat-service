package org.example.chatapp.models;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("chat")
public class Chat {
    @PrimaryKey
    private UUID chatId;
    private UUID userId;
    private Instant createdTime;

    public Chat() {}



    public Chat(UUID chatId, UUID userId, Instant createdTime) {
        this.chatId = chatId;
        this.userId = userId;
        this.createdTime = createdTime;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }
}