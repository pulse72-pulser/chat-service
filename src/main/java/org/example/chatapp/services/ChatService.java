package org.example.chatapp.services;

import org.example.chatapp.config.AstraDbConfig;
import org.example.chatapp.dto.*;
import org.example.chatapp.models.Chat;
import org.example.chatapp.models.UserChat;
import org.example.chatapp.repository.ChatRepository;
import org.example.chatapp.repository.UserChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(AstraDbConfig.class);
//    private final EmbeddingService embeddingService;

    private final ChatApiService chatApiService;
    private final ChatRepository chatRepository;
    private final UserChatRepository userChatRepository;

    public ChatService( ChatApiService chatApiService, ChatRepository chatRepository, UserChatRepository userChatRepository) {
//        this.embeddingService = embeddingService;
        this.chatApiService = chatApiService;
        this.chatRepository = chatRepository;
        this.userChatRepository = userChatRepository;
    }

    // FIXME: add the chat name here
    public ChatCreated createChat(UUID userId, CreateNewChat newChat) {
        Chat chat = new Chat();
        chat.setChatId(UUID.randomUUID());
        chat.setUserId(userId);
        chat.setCreatedTime(Instant.now());
        chat = chatRepository.save(chat);


        return ChatCreated.builder()
                .chatName("optional")
                .chatId(chat.getChatId().toString())
                .userId(chat.getUserId().toString())
                .createdAt(chat.getCreatedTime().toString())
                .message("new chat created.")
                .build();
    }

    public GetChats getChatIdsByUserId(UUID userId) {
        List<Chat> chats = chatRepository.findByUserId(userId);
        List<GetChat> chatList = new ArrayList<>();

        for (Chat chat : chats) {
            chatList.add(GetChat.builder()
                        .chatId(chat.getChatId().toString())
                        .chatName("optional")
                        .createdAt(chat.getCreatedTime().toString())
                        .userId(chat.getUserId().toString())
                        .build());
        }

        return GetChats.builder()
                .chats(chatList)
                .build();
    }

    public GetChat getChatById(UUID chatId,UUID userId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);

        if (chat == null) {
            throw new IllegalArgumentException("invalid chat id");
        }
        if (!chat.getUserId().equals(userId)) {
            throw new IllegalArgumentException("invalid user id");
        }

        return GetChat.builder()
                .chatId(chat.getChatId().toString())
                .chatName("optional")
                .createdAt(chat.getCreatedTime().toString())
                .userId(chat.getUserId().toString())
                .build();
    }

    public ReplyMessage processChat(UUID userId, UUID chatId, String userMessage) {
        // Check if a chat record exists with the given chatId and userId
        Optional<Chat> existingChat = chatRepository.findByChatId(chatId);
        if (existingChat.isEmpty()) {
            throw new IllegalArgumentException("Invalid chatId or userId");
        }

        // Generate embeddings for the user message
//        List<Double> userEmbedding = Arrays.stream(embeddingService.generateEmbedding(userMessage))
//                .boxed()
//                .collect(Collectors.toList());
        List<Double> userEmbedding = null;

        // Get the response from the chat model (hardcoded for testing)
//        String botResponse = "This is a hardcoded response from the chat model.";

        String botResponse = chatApiService.getResponseFromBot(userMessage);

        // Generate embeddings for the bot response
//        List<Double> botEmbedding = Arrays.stream(embeddingService.generateEmbedding(botResponse))
//                .boxed()
//                .collect(Collectors.toList());


        // Store user chat in the database
        UserChat userChat = new UserChat();
        userChat.setId(UUID.randomUUID());
        userChat.setUserId(userId);
        userChat.setChatId(chatId);
        userChat.setRole("user");
        userChat.setCreatedTime(Instant.now());
        userChat.setTextEmbedding(null);
        userChatRepository.save(userChat);

        // Store bot chat in the database
        UserChat botChat = new UserChat();
        botChat.setId(UUID.randomUUID());
        botChat.setUserId(userId);
        botChat.setChatId(chatId);
        botChat.setRole("bot");
        botChat.setCreatedTime(Instant.now());
        botChat.setTextEmbedding(null);
        userChatRepository.save(botChat);

//        return botResponse;

        return ReplyMessage.builder()
                .text(botResponse)
                .author("bot")
                .createdAt(botChat.getCreatedTime().toString())
                .build();
    }

}