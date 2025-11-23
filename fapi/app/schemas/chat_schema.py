from pydantic import BaseModel

# Spring -> FastAPI (요청)
class JudgeRequest(BaseModel):
    user_answer: str       # 사용자 답: "I am hungry"
    quest_criteria: str    # 채점 기준: "공손한 표현인가?"

# FastAPI -> Spring (응답)
class JudgeResponse(BaseModel):
    result: str            # "PASS" or "FAIL"
    feedback: str          # "조금 더 공손하게 말해보세요."