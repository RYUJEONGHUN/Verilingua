package com.aiblockchain.aiblockchainservice.domain.quest.controller;

import com.aiblockchain.aiblockchainservice.domain.quest.dto.AiDto;
import com.aiblockchain.aiblockchainservice.domain.quest.service.QuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    @PostMapping("/{questId}/submit")
    public ResponseEntity<AiDto.JudgeResponse> submitAnswer(
            @AuthenticationPrincipal OAuth2User principal, // 로그인한 유저 정보 자동 주입
            @PathVariable Long questId,
            @RequestBody Map<String, String> body
    ) {
        log.info("Principal: {}", principal); // 로그 추가
        String email = principal.getAttribute("email");
        String userAnswer = body.get("user_answer");

        AiDto.JudgeResponse response = questService.submitQuest(email, questId, userAnswer);
        return ResponseEntity.ok(response);
    }
}