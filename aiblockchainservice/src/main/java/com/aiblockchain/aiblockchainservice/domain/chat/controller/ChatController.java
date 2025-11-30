package com.aiblockchain.aiblockchainservice.domain.chat.controller;

import com.aiblockchain.aiblockchainservice.domain.chat.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final WebClient.Builder webClientBuilder;

    @Value("${fastapi.url}")
    private String fastapiUrl;

    @PostMapping("/talk")
    public ResponseEntity<Map> smallTalk(@RequestBody MessageDto messageDto, @AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");

        // FastAPI에 보낼 데이터 설정
        messageDto.setUser_id(email);

        // FastAPI의 '스몰토크' 엔드포인트 호출
        Map response = webClientBuilder.build()
                .post()
                .uri(fastapiUrl + "/api/v1/ai/talk")
                .bodyValue(messageDto) // DTO 객체를 그대로 전송
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return ResponseEntity.ok(response);
    }
}