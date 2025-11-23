package com.aiblockchain.aiblockchainservice.domain.quest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AiDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JudgeRequest {
        private String user_answer;
        private String quest_criteria;
    }

    @Data
    @NoArgsConstructor
    public static class JudgeResponse {
        private String result;   // "PASS" or "FAIL"
        private String feedback;
    }
}