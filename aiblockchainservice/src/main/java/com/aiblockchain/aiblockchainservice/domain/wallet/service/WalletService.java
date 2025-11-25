package com.aiblockchain.aiblockchainservice.domain.wallet.service;

import com.aiblockchain.aiblockchainservice.domain.wallet.dto.WalletDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WebClient.Builder webClientBuilder;

    @Value("${fastapi.url}") // application.yml에서 가져옴
    private String fastapiUrl;

    public WalletDto createWallet() {
        String requestUrl = fastapiUrl + "/api/v1/blockchain/create";
        log.info("🚀 지갑 생성 요청: {}", requestUrl);

        try {
            return webClientBuilder.build()
                    .post()
                    .uri(requestUrl)
                    .retrieve()
                    .bodyToMono(WalletDto.class)
                    .block(); // 동기 처리 (지갑 없으면 로그인 못하게 막아야 하므로)
        } catch (Exception e) {
            log.error("❌ 지갑 생성 실패", e);
            throw new RuntimeException("지갑 생성 서버 오류");
        }
    }

    // 🚀 [추가] SBT 발행 요청
    public String mintSBT(String walletAddress) {
        String requestUrl = fastapiUrl + "/api/v1/blockchain/mint";
        log.info("🏆 SBT 발행 요청 중... 대상: {}", walletAddress);

        // 보낼 데이터 (JSON)
        Map<String, String> body = Map.of("target_address", walletAddress);

        try {
            // 응답 받기 (Map으로 받아서 tx_hash만 꺼냄)
            Map response = webClientBuilder.build()
                    .post()
                    .uri(requestUrl)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return (String) response.get("tx_hash");

        } catch (Exception e) {
            log.error("❌ SBT 발행 실패", e);
            throw new RuntimeException("SBT 발행 중 오류 발생");
        }
    }
}
