package com.aiblockchain.aiblockchainservice.domain.quest.repository;

import com.aiblockchain.aiblockchainservice.domain.quest.entity.Quest;
import com.aiblockchain.aiblockchainservice.domain.quest.entity.QuestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestLogRepository extends JpaRepository<QuestLog, Long> {

}
