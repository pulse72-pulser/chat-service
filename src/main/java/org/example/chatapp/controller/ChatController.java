package org.example.chatapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.chatapp.dto.ChatInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/chat")
public class ChatController {
    @RequestMapping("/my")
    public ResponseEntity<ChatInfo> chatInfo(@AuthenticationPrincipal Jwt jwt){
        Map<String, Object> claims = jwt.getClaims();
        String sub = (String) claims.get("sub");
        log.info("User sub: {}", sub);
        return ResponseEntity.ok(ChatInfo.builder().chatId("ABC").build());
    }

}
