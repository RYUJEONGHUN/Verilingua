package com.aiblockchain.aiblockchainservice.global.jwt;

import com.aiblockchain.aiblockchainservice.domain.user.entity.Role;
import com.aiblockchain.aiblockchainservice.domain.user.entity.User;
import com.aiblockchain.aiblockchainservice.global.auth.dto.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 헤더에서 토큰 꺼내기
        String authorization = request.getHeader("Authorization");

        // 2. 토큰이 없거나 Bearer로 시작하지 않으면 통과
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        // 3. 토큰 만료 검증
        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. 토큰에서 정보 추출
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        // 5. 임시 유저 객체 생성 (DB 조회 안 함 -> 성능 최적화)
        User user = User.builder()
                .email(email)
                .role(Role.valueOf(role))
                .build();

        Map<String, Object> attributes = Map.of("email", email);
        // 6. 시큐리티 세션에 강제 등록 (이 요청 동안만 로그인 상태 유지)
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(user, attributes);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}