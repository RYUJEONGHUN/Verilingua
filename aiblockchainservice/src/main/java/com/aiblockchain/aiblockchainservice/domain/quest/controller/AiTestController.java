package com.aiblockchain.aiblockchainservice.domain.quest.controller;

import com.aiblockchain.aiblockchainservice.domain.quest.dto.AiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping; // 👈 PostMapping 임포트
import org.springframework.web.bind.annotation.RequestBody; // 👈 RequestBody 임포트
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AiTestController {

    private final WebClient.Builder webClientBuilder;

    @Value("${fastapi.url}")
    private String fastapiUrl;

    // 이제 Postman에서 Body에 JSON을 담아서 보내야 합니다.
    @PostMapping("/test/ai")
    public ResponseEntity<?> testAiConnection(@RequestBody AiDto.JudgeRequest request) {

        String requestUrl = fastapiUrl + "/api/v1/ai/judge";

        log.info("🚀 Spring -> FastAPI 요청 주소: {}", requestUrl);
        log.info("📩 받을 데이터: {}", request); // Postman에서 보낸 데이터 로그 찍기

        try {
            // FastAPI 호출 (그대로 전달)
            AiDto.JudgeResponse response = webClientBuilder.build()
                    .post()
                    .uri(requestUrl)
                    .bodyValue(request) // 👈 Postman에서 받은 데이터를 그대로 FastAPI로 토스
                    .retrieve()
                    .bodyToMono(AiDto.JudgeResponse.class)
                    .block();

            log.info("✅ FastAPI 응답: {}", response);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ 연동 실패", e);
            return ResponseEntity.status(500).body("연동 실패: " + e.getMessage());
        }
    }
}