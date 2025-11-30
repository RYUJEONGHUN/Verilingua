package com.aiblockchain.aiblockchainservice.global.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenRefreshRequest {
    @NotBlank(message = "이메일은 필수값입니다")
    private String email;
}
