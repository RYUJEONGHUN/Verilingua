package com.aiblockchain.aiblockchainservice.global.auth.handler;

import com.aiblockchain.aiblockchainservice.domain.user.entity.User;
import com.aiblockchain.aiblockchainservice.domain.user.repository.UserRepository;
import com.aiblockchain.aiblockchainservice.domain.wallet.dto.WalletDto;
import com.aiblockchain.aiblockchainservice.domain.wallet.service.WalletService;
import com.aiblockchain.aiblockchainservice.global.auth.dto.CustomOAuth2User;
import com.aiblockchain.aiblockchainservice.global.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final StringRedisTemplate redisTemplate; // Redis 사용!
    private final UserRepository userRepository;
    private final WalletService walletService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 1. 유저 정보 추출
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String email = customUserDetails.getEmail();
        String role = customUserDetails.getAuthorities().iterator().next().getAuthority();

        User user=userRepository.findByEmail(email).orElseThrow();

        if(user.getWalletAddress()==null){
            WalletDto newWallet=walletService.createWallet();
            user.registerWallet(newWallet.getWalletAddress(),newWallet.getPrivateKey());
            userRepository.save(user);
            log.info(" 지갑 생성 및 저장 완료: {}", newWallet.getWalletAddress());
        }else {
            log.info(" 기존 지갑 보유 유저: {}", user.getWalletAddress());
        }
        user.failChallenge(); //  currentQuestStep=0
        userRepository.save(user);

        log.info(" 유저 퀘스트 상태 초기화 완료: {}", email);

        // 2. Access Token 생성 (1시간)
        String accessToken = jwtUtil.createJwt(email, role, 60 * 60 * 1000L);

        // 3. Refresh Token 생성 (14일)
        String refreshToken = jwtUtil.createJwt(email, role, 14 * 24 * 60 * 60 * 1000L);

        // 4. Refresh Token을 Redis에 저장 (Key: "RT:이메일", Value: 토큰, TTL: 14일)
        redisTemplate.opsForValue()
                .set("RT:" + email, refreshToken, 14, TimeUnit.DAYS);

        log.info(" 로그인 성공! Access Token 발급, Refresh Token Redis 저장 완료. Email: {}", email);

        // 5. 프론트엔드로 리다이렉트 (Access Token만 쿼리 파라미터로 전달)
        // 주의: http://localhost:8080/login-success 는 임시 확인용 주소입니다.

        // 로컬전용
        //String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth/callback")
        //        .queryParam("accessToken", accessToken)
        //        .build().toUriString();

        String targetUrl = UriComponentsBuilder.fromUriString(
                        "https://verilingua-front.vercel.app/login")   // 👈 /login 으로 보냄
                .queryParam("accessToken", accessToken)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }
}