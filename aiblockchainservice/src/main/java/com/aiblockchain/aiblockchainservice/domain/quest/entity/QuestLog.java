package com.aiblockchain.aiblockchainservice.domain.quest.entity;

import com.aiblockchain.aiblockchainservice.domain.user.entity.User;
import com.aiblockchain.aiblockchainservice.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quest_logs")
@Getter
@NoArgsConstructor
public class QuestLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    private Quest quest;

    @Column(columnDefinition = "TEXT")
    private String userAnswer; // 사용자가 쓴 답

    @Enumerated(EnumType.STRING)
    private QuestResult status; // PASS, FAIL

    @Column(columnDefinition = "TEXT")
    private String aiFeedback; // AI가 준 피드백

    public enum QuestResult {
        PASS, FAIL
    }

    @Builder
    public QuestLog(User user, Quest quest, String userAnswer, QuestResult status, String aiFeedback) {
        this.user = user;
        this.quest = quest;
        this.userAnswer = userAnswer;
        this.status = status;
        this.aiFeedback = aiFeedback;
    }
}