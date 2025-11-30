from fastapi import APIRouter, HTTPException
from app.schemas.chat_schema import JudgeRequest, JudgeResponse
from app.schemas.chat2_schema import TalkRequest
from app.services.openai_service import evaluate_answer
from openai import AsyncOpenAI 
import os
from dotenv import load_dotenv
from app.services.memory_service import add_message_to_history, get_chat_history


load_dotenv()

router = APIRouter()

client = AsyncOpenAI(api_key=os.getenv("OPENAI_API_KEY"))

@router.post("/talk")
async def small_talk(request: TalkRequest):
    # 1. 사용자 메시지 저장
    await add_message_to_history(request.user_id, "user", request.message)
    
    # 2. 이전 대화 기록 가져오기
    history = await get_chat_history(request.user_id)
    
    # 3. 시스템 프롬프트 (페르소나)
    system_message = {"role": "system", "content": "You are Veri, a friendly English tutor. Remember the user's name and context."}
    
    # 4. GPT에게 보낼 메시지 구성 (시스템 + 과거 대화 + 현재 질문)
    messages = [system_message] + history

    # 5. GPT 호출
    response = await client.chat.completions.create(
        model="gpt-4o-mini",
        messages=messages
    )
    
    ai_reply = response.choices[0].message.content
    
    # 6. AI 답변도 저장
    await add_message_to_history(request.user_id, "assistant", ai_reply)
    
    return {"reply": ai_reply}


@router.post("/judge", response_model=JudgeResponse)
async def judge_quest(request: JudgeRequest):
    try:
        # AI 서비스 호출
        result = await evaluate_answer(request.user_answer, request.quest_criteria)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))