package com.aiblockchain.aiblockchainservice.domain.quest.service;

import com.aiblockchain.aiblockchainservice.domain.quest.dto.AiDto;
import com.aiblockchain.aiblockchainservice.domain.quest.dto.QuestDto;
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
            //  마지막 퀘스트(예: 3번)를 깼다면? -> 보상 지급!
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
    // 레벨 도전 시작 (게이팅 로직 포함)
    @Transactional
    public void startChallenge(String email, int level) {
        User user = userRepository.findByEmail(email).orElseThrow();

        // L2 잠금 해제 조건 확인
        if (level == 2) {
            if (user.getL1TokenCount() < 3) {
                throw new IllegalArgumentException("L1 토큰이 3개 이상 필요합니다!");
            }
        }

        // 도전 상태 설정 (1단계부터 시작)
        user.startChallenge(level);
        userRepository.save(user);
    }

    //  현재 풀어야 할 문제 가져오기
    @Transactional(readOnly = true)
    public QuestDto getCurrentQuest(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        if (user.getCurrentChallengeLevel() == null) {
            return null; // 진행 중인 퀘스트 없음
        }

        // 유저의 현재 상태(Level, Step)에 맞는 퀘스트 DB에서 조회
        // (Step은 0부터 시작하므로, 문제는 Step + 1을 가져와야 함)
        int nextStep = user.getCurrentQuestStep() + 1;

        return questRepository.findByLevelAndStep(user.getCurrentChallengeLevel(), nextStep)
                .map(quest -> new QuestDto(quest.getId(), quest.getTitle(), quest.getContent()))
                .orElse(null); // 더 이상 문제가 없으면(다 깬 경우) null
    }
}