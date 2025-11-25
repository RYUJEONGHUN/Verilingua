package com.aiblockchain.aiblockchainservice.domain.quest.service;

import com.aiblockchain.aiblockchainservice.domain.quest.dto.AiDto;
import com.aiblockchain.aiblockchainservice.domain.quest.entity.Quest;
import com.aiblockchain.aiblockchainservice.domain.quest.entity.QuestLog;
import com.aiblockchain.aiblockchainservice.domain.quest.repository.QuestLogRepository;
import com.aiblockchain.aiblockchainservice.domain.quest.repository.QuestRepository;
import com.aiblockchain.aiblockchainservice.domain.user.entity.User;
import com.aiblockchain.aiblockchainservice.domain.user.repository.UserRepository;
import com.aiblockchain.aiblockchainservice.domain.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final QuestLogRepository questLogRepository;
    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;
    private final WalletService walletService; // 👈 추가 주입

    @Value("${fastapi.url}")
    private String fastapiUrl;

    @Transactional
    public AiDto.JudgeResponse submitQuest(String email, Long questId, String userAnswer) {
        //  유저 & 퀘스트 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        Quest quest = questRepository.findById(questId).orElseThrow(() -> new IllegalArgumentException("퀘스트 없음"));

        //  AI 심판에게 요청 (FastAPI)
        AiDto.JudgeRequest request = new AiDto.JudgeRequest(userAnswer, quest.getJudgeCriteria());

        AiDto.JudgeResponse aiResponse = webClientBuilder.build()
                .post()
                .uri(fastapiUrl + "/api/v1/ai/judge")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AiDto.JudgeResponse.class)
                .block();

        //  로그 저장 (이력 관리)
        QuestLog logEntry = QuestLog.builder()
                .user(user)
                .quest(quest)
                .userAnswer(userAnswer)
                .aiFeedback(aiResponse.getFeedback())
                .status(aiResponse.getResult().equals("PASS") ? QuestLog.QuestResult.PASS : QuestLog.QuestResult.FAIL)
                .build();
        questLogRepository.save(logEntry);

        // . 유저 상태 업데이트 (One Miss Out 로직)
        if ("PASS".equals(aiResponse.getResult())) {
            user.progressNextStep(); // 다음 단계로!
            // 🚀 [핵심] 마지막 퀘스트(예: 3번)를 깼다면? -> 보상 지급!
            if (user.getCurrentQuestStep() >= 3) {
                log.info("🎉 모든 퀘스트 완료! SBT를 발행합니다.");

                String txHash = walletService.mintSBT(user.getWalletAddress());

                user.addToken(1); // DB에 토큰 개수 증가
                user.startChallenge(1); // 레벨 초기화 or 다음 레벨로 (로직에 따라)

                // 피드백에 발행 사실 추가
                aiResponse.setFeedback(aiResponse.getFeedback() +
                        "\n\n🏆 축하합니다! 모든 퀘스트를 완료하여 SBT가 발행되었습니다! (TX: " + txHash + ")");
            }
        } else {
            user.failChallenge(); // 실패! 처음부터 다시
        }
        userRepository.save(user);

        return aiResponse;
    }
}