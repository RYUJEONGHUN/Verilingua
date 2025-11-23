package com.aiblockchain.aiblockchainservice.domain.quest.repository;

import com.aiblockchain.aiblockchainservice.domain.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    Optional<Quest> findByLevelAndStep(Integer level, Integer step);
}