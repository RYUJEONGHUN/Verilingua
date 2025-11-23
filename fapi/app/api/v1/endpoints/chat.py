from fastapi import APIRouter, HTTPException
from app.schemas.chat_schema import JudgeRequest, JudgeResponse
from app.services.openai_service import evaluate_answer

router = APIRouter()

@router.post("/judge", response_model=JudgeResponse)
async def judge_quest(request: JudgeRequest):
    try:
        # AI 서비스 호출
        result = await evaluate_answer(request.user_answer, request.quest_criteria)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))