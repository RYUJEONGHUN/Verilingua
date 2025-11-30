package com.aiblockchain.aiblockchainservice.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private String email;
    private String name;
    private String walletAddress;
    private Integer l1TokenCount;
    private String etherscanUrl; // 센스 있게 링크까지 만들어 줍시다
}