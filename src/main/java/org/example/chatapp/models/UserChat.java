package org.example.chatapp.models;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("user_chat")
public class UserChat {
    @PrimaryKey
    private UUID id;

    private UUID userId;

    private UUID chatId;
    private String role;
    private Instant createdTime;
    private List<Double> textEmbedding;

    public UserChat() {
    }

    public UserChat(UUID id, UUID userId, UUID chatId, String role, Instant createdTime, List<Double> textEmbedding) {
        this.id = id;
        this.userId = userId;
        this.chatId = chatId;
        this.role = role;
        this.createdTime = createdTime;
        this.textEmbedding = textEmbedding;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public List<Double> getTextEmbedding() {
        return textEmbedding;
    }

    public void setTextEmbedding(List<Double> textEmbedding) {
        this.textEmbedding = textEmbedding;
    }
}