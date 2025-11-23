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
    
    # (프롬프트 부분은 아주 잘 짜셨습니다! 그대로 두세요.)
    prompt = f"""
    You are a strict English examiner.
    [Criteria]: {criteria}
    [User Answer]: "{answer}"
    
    Evaluate based heavily on the criteria.
    Respond ONLY in JSON format: {{"result": "PASS" or "FAIL", "feedback": "reason"}}
    """

    # 3. await 키워드 붙이기 (변경됨)
    # (네트워크 요청을 기다리는 동안 다른 일을 할 수 있게 해줍니다)
    response = await client.chat.completions.create(
        model="gpt-4o",
        messages=[{"role": "system", "content": prompt}],
        response_format={"type": "json_object"}
    )
    
    return json.loads(response.choices[0].message.content)