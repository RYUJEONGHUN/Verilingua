package com.aiblockchain.aiblockchainservice.domain.quest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestDto {
    private Long id;
    private String title;
    private String content;
}
