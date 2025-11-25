package com.aiblockchain.aiblockchainservice.domain.user.entity;

//import com.aiblockchain.aiblockchainservice.global.common.BaseEntity; // (생성시간 필요시)
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // 로그인 ID 역할

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 지갑 주소 (나중에 입력받으므로 nullable)
    @Column(unique = true)
    private String walletAddress;

    //개인키 저장
    private String privateKey;

    // OAuth 제공자 정보 (google, sub)
    private String provider;
    private String providerId;

    // --- 생성자 (Builder) ---
    @Builder
    public User(String email, String name, Role role, String provider, String providerId) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    // --- 비즈니스 로직 메서드 ---
    public void updateName(String name) {
        this.name = name;
    }

    public void registerWallet(String walletAddress, String privateKey) {
        this.walletAddress = walletAddress;
        this.privateKey = privateKey;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    // --- 게임 상태 (State) ---
    private Integer currentChallengeLevel; // 현재 도전 중인 레벨
    private Integer currentQuestStep = 0;  // 현재 성공한 단계 (0~2)

    // --- 자산 (Assets - SBT 개수) ---
    @Column(nullable = false) private Integer l1TokenCount = 0;
    @Column(nullable = false) private Integer l2TokenCount = 0;


    // 퀘스트 진행 메서드
    public void startChallenge(int level) {
        this.currentChallengeLevel = level;
        this.currentQuestStep = 0;
    }

    public void progressNextStep() {
        this.currentQuestStep++;
    }

    public void failChallenge() {
        this.currentChallengeLevel = null;
        this.currentQuestStep = 0;
    }

    public void addToken(int level) {
        switch (level) {
            case 1 -> this.l1TokenCount++;
            case 2 -> this.l2TokenCount++;
        }
    }


}