-- Level 1: 정확성 (단어 치환)
INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 1, 1, 'L1-Q1: Word Substitution',
       'Change the word "happy" to "delighted" in the sentence: "I am very happy to meet you."',
       '1. The answer must contain the word "delighted". 2. The grammar must be correct. 3. The meaning must be positive.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 1 AND step = 1);

INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 1, 2, 'L1-Q2: Grammar Fix',
       'Correct the grammar: "He don''t like apples."',
       '1. The answer should use "doesn''t" or "does not". 2. The verb "like" should remain in base form. 3. The object "apples" should be plural or singular generic.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 1 AND step = 2);

INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 1, 3, 'L1-Q3: Wallet Meaning',
       'Rewrite this sentence so that it explains what a crypto wallet really stores: "A crypto wallet stores my coins inside the app."',
       '1. The answer must say that a wallet stores private keys or access to coins, not the coins physically inside the app. 2. The explanation must still mention coins or crypto assets in some way. 3. Grammar must be correct and meaning must be clear.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 1 AND step = 3);

-- Level 2: 유창성
INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 2, 1, 'L2-Q1: Collocation',
       'Complete the sentence naturally: "Can you _____ a secret?"',
       '1. The answer must use the verb "keep". 2. Accept "keep". Reject "hold", "save", "protect".'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 2 AND step = 1);

-- Level 2: 유창성
INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 2, 2, 'L2-Q2: Phrasal Verb',
       'Complete the sentence naturally: "I am really looking _____ to the trip this weekend."',
       '1. The answer must use the word "forward". 2. Accept only "forward". Reject answers like "for it", "ahead", or anything else.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 2 AND step = 2);

INSERT INTO quests (level, step, title, content, judge_criteria)
SELECT 2, 3, 'L2-Q3: Adverb Choice',
       'Complete the sentence so it sounds natural: "She speaks English very ____."',
       '1. The answer must use the adverb "fluently". 2. Accept "fluently". Reject "fluent", "well", or other words.'
    WHERE NOT EXISTS (SELECT 1 FROM quests WHERE level = 2 AND step = 3);