-- Level 1: 기초 개념 + 빈칸/짧은 답 (블록체인)

INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 1, 1, 'L1-Q1: What is a Blockchain?',
       'Complete the sentence: "A blockchain is a shared, ______ ledger that records transactions in blocks."',
       '1. The blank should be filled with a word that shows the ledger is not controlled by a single central server, such as "distributed" or "decentralized". 2. Answers like "distributed", "decentralized", or similar adjectives are acceptable. 3. The overall meaning must describe a shared ledger used to record transactions.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 1 AND step = 1);

INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 1, 2, 'L1-Q2: Gas in Ethereum',
       'Answer in one word: "In Ethereum, what is the unit that measures the computational cost of executing operations in a smart contract?"',
       '1. The correct answer is "gas". 2. Accept lowercase or uppercase variants such as "Gas" or "GAS". 3. Answers like "ether" or "fee" without the word "gas" should not be accepted.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 1 AND step = 2);


INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 1, 3, 'L1-Q3: Why Trust Data on a Public Blockchain',
       'In 3–5 sentences, explain why people can trust the data stored on a public blockchain. Mention at least two technical reasons (for example: how blocks are linked, how consensus works, or how many nodes share the data).',
       '1. The answer should mention at least two technical reasons for trust, such as hash-linked blocks, consensus mechanisms, or replication across many nodes. 2. It should clearly explain that changing past data is extremely difficult because blocks are linked by hashes or similar cryptographic fingerprints. 3. It should mention that many independent nodes validate and store the same data, so a single attacker cannot easily change the history. 4. The explanation does not need to be perfectly detailed, but the overall meaning must show an understanding of why public blockchains provide integrity and tamper resistance.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 1 AND step = 3);


------------------------------------------------------
-- Level 2: 유창성 + 응용 개념 (블록체인) – 빈칸 + 짧은 개념 설명

INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 2, 1, 'L2-Q1: Ethereum Account Types',
       'Fill in the blank: "In Ethereum, accounts are divided into externally owned accounts (EOA) and ______ accounts, which contain code and cannot start transactions by themselves."',
       '1. The answer should refer to "contract accounts" or "smart contract accounts". 2. Variants such as "contract" or "smart contract" are acceptable if the meaning is clear. 3. Answers that mention "EOA", "wallet", or general "account" without the contract concept should not be accepted.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 2 AND step = 1);

INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 2, 2, 'L2-Q2: One-Way Relationship Between Keys',
       'Answer TRUE or FALSE: "In a secure blockchain system, it is easy to calculate the public key from the private key, but it is practically impossible to calculate the private key back from the public key."',
       '1. The correct answer is TRUE. 2. Accept "TRUE", "True", or "T" as correct. 3. Any clearly false-like answer such as "FALSE" or "F" should be marked incorrect.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 2 AND step = 2);


INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 2, 3, 'L2-Q3: Layer 2',
       'Explain the idea of “Layer 2” in simple words.',
       '1. The answer should roughly describe Layer 2 as a network or solution built on top of a Layer 1 main chain (helper layer, second layer, or similar wording is acceptable). 2. The answer should mention at least one reason why Layer 2 is needed, such as improving speed, handling more transactions, or reducing fees. 3. The answer should mention that Layer 1 is still the main or final chain where results or summaries are recorded, or that Layer 2 depends on Layer 1 for final security. 4. The answer does not need to be technically perfect; as long as the overall meaning matches these ideas in simple language, it can be marked as correct.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 2 AND step = 3);
