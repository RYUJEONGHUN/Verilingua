package com.aiblockchain.aiblockchainservice.global.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String email; // Key: 사용자 이메일 (PK로 사용)

    private String token; // Value: 실제 리프레시 토큰 값

    // 토큰 만료 시간 (선택사항, 3주 MVP엔 없어도 됨)
    // private Long expiration;

    @Builder
    public RefreshToken(String email, String token) {
        this.email = email;
        this.token = token;
    }

    // 토큰 교체 메서드 (로그인 할 때마다 업데이트)
    public void updateToken(String token) {
        this.token = token;
    }
}