package com.aiblockchain.aiblockchainservice.global.auth.controller;


import com.aiblockchain.aiblockchainservice.global.auth.dto.TokenRefreshRequest;
import com.aiblockchain.aiblockchainservice.global.jwt.JWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JWTUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<Object, Object> redisTemplate;

    @PostMapping("/request")
    public ResponseEntity<?> refresh(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        String email = tokenRefreshRequest.getEmail();

        String refreshToken = (String) redisTemplate.opsForValue().get("RT:"+email);

        if (refreshToken == null) {
            return ResponseEntity.status(401).body("Refresh Token Expired");
        }

        // 3. 새 액세스 토큰 발급
        String newAccessToken = jwtUtil.createJwt(email, "ROLE_USER", 60 * 60 * 1000L);

        return ResponseEntity.ok(Map.of("accessToken",newAccessToken));
    }

}
