package org.example.chatapp.services;

import org.example.chatapp.config.AstraDbConfig;
import org.example.chatapp.models.Chat;
import org.example.chatapp.models.UserChat;
import org.example.chatapp.repository.ChatRepository;
import org.example.chatapp.repository.UserChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(AstraDbConfig.class);
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

    public List<UUID> getChatIdsByUserId(UUID userId) {
        List<Chat> chats = chatRepository.findByUserId(userId);
        return chats.stream()
                .map(Chat::getChatId)
                .collect(Collectors.toList());
    }

    public String processChat(UUID userId, UUID chatId, String userMessage) {
        // Check if a chat record exists with the given chatId and userId
        Optional<Chat> existingChat = chatRepository.findByChatId(chatId);
        if (existingChat.isEmpty()) {
            log.error(String.valueOf(chatId) + " " + String.valueOf(userId));
            throw new IllegalArgumentException("Invalid chatId or userId");
        }

        // Generate embeddings for the user message
        List<Double> userEmbedding = Arrays.stream(embeddingService.generateEmbedding(userMessage))
                .boxed()
                .collect(Collectors.toList());

        // Get the response from the chat model (hardcoded for testing)
        String botResponse = "This is a hardcoded response from the chat model.";

        // Generate embeddings for the bot response
        List<Double> botEmbedding = Arrays.stream(embeddingService.generateEmbedding(botResponse))
                .boxed()
                .collect(Collectors.toList());

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