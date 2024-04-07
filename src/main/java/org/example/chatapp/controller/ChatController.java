package org.example.chatapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.chatapp.dto.ChatInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/v1/chat")
public class ChatController {
    @RequestMapping("/my")
    public ResponseEntity<ChatInfo> chatInfo(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(principal.toString());
        return ResponseEntity.ok(ChatInfo.builder().chatId("ABC").build());
    }

}
