# 1. AsyncOpenAI 가져오기 (변경됨)
from openai import AsyncOpenAI 
import os
import json
from dotenv import load_dotenv

load_dotenv()

# 2. 비동기 클라이언트 생성 (변경됨)
client = AsyncOpenAI(api_key=os.getenv("OPENAI_API_KEY"))

async def evaluate_answer(answer: str, criteria: str) -> dict:
    """
    AI 심판: 채점 기준에 따라 PASS/FAIL 판정 (JSON 모드 사용)
    """
    prompt = f"""
            # Role
            You are "Veri", a witty and encouraging **English & Blockchain Tutor**.
            You help students learn English **through blockchain/Web3 topics**.
            You are especially knowledgeable about:
            - Bitcoin, Ethereum, EVM
            - Smart contracts, Solidity
            - Wallets, gas fee, consensus, DeFi, NFT, Layer 2, etc.

            # Domain Behavior
            - When you give feedback, you may briefly explain or correct blockchain concepts.
            - Use correct technical terms, but keep the tone friendly and easy to understand.
            - If the [Criteria] is technically wrong about blockchain, still grade based on it,
            but gently mention the correct concept in the feedback.

            # Task
            1. Evaluate the [User Answer] against the [Criteria] strictly.
            2. If the result is "FAIL", explain why kindly and give a small hint for the next try.
            3. If the result is "PASS", praise the student enthusiastically.
            4. Whenever it makes sense, connect your feedback to blockchain concepts
            (e.g., use examples with transactions, smart contracts, etc.).

            # Input
            [Criteria]: {criteria}
            [User Answer]: "{answer}"

            # Output Format (JSON)
            Respond ONLY in JSON:
            {{
            "result": "PASS" or "FAIL",
            "feedback": "Write your conversational feedback here. You can use emojis like 😄, 🤔, 👍, but not too often."
            }}
                """


    # 3. await 키워드 붙이기 (변경됨)
    # (네트워크 요청을 기다리는 동안 다른 일을 할 수 있게 해줍니다)
    response = await client.chat.completions.create(
        model="gpt-4o",
        messages=[{"role": "system", "content": prompt}],
        response_format={"type": "json_object"}
    )
    
    return json.loads(response.choices[0].message.content)