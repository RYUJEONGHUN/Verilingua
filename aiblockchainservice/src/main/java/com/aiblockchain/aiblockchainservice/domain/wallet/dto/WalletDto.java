package com.aiblockchain.aiblockchainservice.domain.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletDto {
    @JsonProperty("address")
    private String walletAddress;

    @JsonProperty("private_key")
    private String privateKey;
}
