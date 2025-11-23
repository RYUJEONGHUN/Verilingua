package com.aiblockchain.aiblockchainservice.domain.quest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quests", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"level", "step"})
})
@Getter
@NoArgsConstructor
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer level; // 1 ~ 2

    @Column(nullable = false)
    private Integer step;  // 1 ~ 5

    @Column(nullable = false)
    private String title;

    // 사용자에게 보여줄 문제
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // AI 심판에게 보낼 채점 기준 (Prompt Template)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String judgeCriteria;
}