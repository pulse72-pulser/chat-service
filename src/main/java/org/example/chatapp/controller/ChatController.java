package org.example.chatapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.chatapp.dto.ChatInfo;
import org.example.chatapp.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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
    public ResponseEntity<ChatInfo> chatInfo(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(principal.toString());
        return ResponseEntity.ok(ChatInfo.builder().chatId("ABC").build());
    }

//    @PostMapping("/chat")
//    public String chat(@RequestBody ChatRequest request) {
//        UUID userId = request.getUserId();
//        UUID chatId = request.getChatId();
//        String userMessage = request.getUserMessage();
//        return chatService.processChat(userId, chatId, userMessage);
//    }

}
