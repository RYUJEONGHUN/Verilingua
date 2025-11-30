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
    You are 'Veri', a witty and encouraging English Tutor.
    Your goal is to evaluate the student's answer based on the criteria, but maintain a fun, conversational tone like a friend.

    # Task
    1. Evaluate the [User Answer] against the [Criteria] strictly.
    2. If the result is 'FAIL', explain why kindly and give a small hint for the next try.
    3. If the result is 'PASS', praise the student enthusiastically.

    # Input
    [Criteria]: {criteria}
    [User Answer]: "{answer}"

    # Output Format (JSON)
    Respond ONLY in JSON.
    {{
        "result": "PASS" or "FAIL", 
        "feedback": "Write your conversational feedback here. You can use emojis like 😄, 🤔, 👍, but not too often"
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