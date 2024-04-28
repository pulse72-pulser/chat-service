package org.example.chatapp.services;

import org.example.chatapp.models.Chat;
import org.example.chatapp.models.UserChat;
import org.example.chatapp.repository.ChatRepository;
import org.example.chatapp.repository.UserChatRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatService {
    private final EmbeddingService embeddingService;
    private final ChatRepository chatRepository;
    private final UserChatRepository userChatRepository;

    public ChatService(EmbeddingService embeddingService, ChatRepository chatRepository, UserChatRepository userChatRepository) {
        this.embeddingService = embeddingService;
        this.chatRepository = chatRepository;
        this.userChatRepository = userChatRepository;
    }

    public Chat createChat(UUID userId) {
        Chat chat = new Chat();
        chat.setChatId(UUID.randomUUID());
        chat.setUserId(userId);
        chat.setCreatedTime(Instant.now());
        return chatRepository.save(chat);
    }

    public String processChat(UUID userId, UUID chatId, String userMessage) {
        // Check if a chat record exists with the given chatId and userId
        Optional<Chat> existingChat = chatRepository.findByChatIdAndUserId(chatId, userId);
        if (existingChat.isEmpty()) {
            throw new IllegalArgumentException("Invalid chatId or userId");
        }

        // Generate embeddings for the user message
        double[] userEmbedding = embeddingService.generateEmbedding(userMessage);

        // Get the response from the chat model (hardcoded for testing)
        String botResponse = "This is a hardcoded response from the chat model.";

        // Generate embeddings for the bot response
        double[] botEmbedding = embeddingService.generateEmbedding(botResponse);

        // Store user chat in the database
        UserChat userChat = new UserChat();
        userChat.setId(UUID.randomUUID());
        userChat.setUserId(userId);
        userChat.setChatId(chatId);
        userChat.setRole("user");
        userChat.setCreatedTime(Instant.now());
        userChat.setTextEmbedding(userEmbedding);
        userChatRepository.save(userChat);

        // Store bot chat in the database
        UserChat botChat = new UserChat();
        botChat.setId(UUID.randomUUID());
        botChat.setUserId(userId);
        botChat.setChatId(chatId);
        botChat.setRole("bot");
        botChat.setCreatedTime(Instant.now());
        botChat.setTextEmbedding(botEmbedding);
        userChatRepository.save(botChat);

        return botResponse;
    }
}