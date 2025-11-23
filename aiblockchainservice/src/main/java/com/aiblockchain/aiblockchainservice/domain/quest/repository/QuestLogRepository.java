package com.aiblockchain.aiblockchainservice.domain.quest.repository;

import com.aiblockchain.aiblockchainservice.domain.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestLogRepository extends JpaRepository<Quest, Long> {

}
