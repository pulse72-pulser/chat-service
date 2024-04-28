package org.example.chatapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.chatapp.dto.ChatInfo;
import org.example.chatapp.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.example.chatapp.models.Chat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/v1/chat")
public class ChatController {


    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    @RequestMapping("/my")
    public ResponseEntity<ChatInfo> chatInfo(@AuthenticationPrincipal Jwt jwt){
        Map<String, Object> claims = jwt.getClaims();
        String sub = (String) claims.get("sub");
        log.info("User sub: {}", sub);
        return ResponseEntity.ok(ChatInfo.builder().chatId("ABC").build());
    }

    @PostMapping("/chats")
    public ResponseEntity<UUID> createChat() {
        // Hardcoded user ID for testing
        UUID userId = UUID.fromString("user_id_value_here");

        Chat createdChat = chatService.createChat(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChat.getChatId());
    }

    @PostMapping("/chats/{chatId}/messages")
    public ResponseEntity<String> sendUserChat(@RequestBody String userMessage) {
        // Hardcoded chat ID and user ID for testing
        UUID chatId = UUID.fromString("chat_id_value_here");
        UUID userId = UUID.fromString("user_id_value_here");

        String botResponse = chatService.processChat(userId, chatId, userMessage);
        return ResponseEntity.ok(botResponse);
    }


}

