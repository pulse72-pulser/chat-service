package org.example.chatapp.test;

import org.example.chatapp.ChatAppApplication;
import org.example.chatapp.config.AstraDbConfig;
import org.example.chatapp.dto.ChatCreated;
import org.example.chatapp.dto.CreateNewChat;
import org.example.chatapp.models.Chat;
import org.example.chatapp.models.UserChat;
import org.example.chatapp.repository.ChatRepository;
import org.example.chatapp.repository.UserChatRepository;
import org.example.chatapp.services.ChatService;
import org.example.chatapp.services.EmbeddingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ChatAppApplication.class)
public class ChatInfoServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AstraDbConfig.class);
    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserChatRepository userChatRepository;

    @Autowired
    private EmbeddingService embeddingService;

    @Test
    public void testChatInitialization() {

        log.info("Starting testChatInitialization");
        // Create a dummy chat
        UUID dummyUserId = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
        CreateNewChat createNewChat = CreateNewChat.builder()
                .chatName("new-chat")
                .build();
        ChatCreated createdChat = chatService.createChat(dummyUserId,createNewChat);
        UUID chatId = UUID.fromString(createdChat.getChatId());

        // Add a dummy test message to the created chat
        String dummyMessage = "This is a dummy test message";
        String botResponse = chatService.processChat(dummyUserId, chatId, dummyMessage);

        // Verify the chat insertion
        Optional<Chat> retrievedChat = chatRepository.findById(chatId);
        assertTrue(retrievedChat.isPresent(), "Chat should be present in the database");
        assertEquals(dummyUserId, retrievedChat.get().getUserId(), "Chat should have the correct user ID");

        // Verify the user chat insertion
        List<UserChat> userChats = userChatRepository.findByChatId(chatId);
        assertFalse(userChats.isEmpty(), "User chats should be present for the chat ID");

        // Verify the user message insertion
        Optional<UserChat> userMessage = userChats.stream()
                .filter(userChat -> userChat.getRole().equals("user"))
                .findFirst();
        assertTrue(userMessage.isPresent(), "User message should be present in the user chats");
//        assertEquals(Arrays.asList(embeddingService.generateEmbedding(dummyMessage)), userMessage.get().getTextEmbedding(), "User message should have the correct text embedding");
        double[] expectedEmbedding = embeddingService.generateEmbedding(dummyMessage);
        List<Double> actualEmbedding = userMessage.get().getTextEmbedding();
        assertArrayEquals(expectedEmbedding, actualEmbedding.stream().mapToDouble(Double::doubleValue).toArray(), "User message should have the correct text embedding");

        // Verify the bot response insertion
        Optional<UserChat> botMessage = userChats.stream()
                .filter(userChat -> userChat.getRole().equals("bot"))
                .findFirst();
        assertTrue(botMessage.isPresent(), "Bot response should be present in the user chats");
//        assertEquals(Arrays.asList(embeddingService.generateEmbedding(botResponse)), botMessage.get().getTextEmbedding(), "Bot response should have the correct text embedding");
        double[] expectedBotEmbedding = embeddingService.generateEmbedding(botResponse);
        List<Double> actualBotEmbedding = botMessage.get().getTextEmbedding();
        assertArrayEquals(expectedBotEmbedding, actualBotEmbedding.stream().mapToDouble(Double::doubleValue).toArray(), "Bot response should have the correct text embedding");

        log.info("Finished testChatInitialization");
    }
}