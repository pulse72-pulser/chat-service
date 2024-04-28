package org.example.chatapp.models;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("user_chat")
public class UserChat {
    @PrimaryKey
    private UUID userId;
    private UUID chatId;
    private String role;
    private Instant createdTime;
    private double[] textEmbedding;

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

    public double[] getTextEmbedding() {
        return textEmbedding;
    }

    public void setTextEmbedding(double[] textEmbedding) {
        this.textEmbedding = textEmbedding;
    }

    // Getters and setters
}