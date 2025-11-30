package com.aiblockchain.aiblockchainservice.domain.user.controller;

import com.aiblockchain.aiblockchainservice.domain.user.dto.UserResponseDto;
import com.aiblockchain.aiblockchainservice.domain.user.entity.User;
import com.aiblockchain.aiblockchainservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(@AuthenticationPrincipal OAuth2User principal) {
        // 로그인 안 된 상태면 에러
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 응답 생성
        UserResponseDto response = UserResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .walletAddress(user.getWalletAddress())
                .l1TokenCount(user.getL1TokenCount())
                // 세폴리아 이더스캔 링크
                .etherscanUrl("https://sepolia.etherscan.io/address/" + user.getWalletAddress())
                .build();

        return ResponseEntity.ok(response);
    }
}