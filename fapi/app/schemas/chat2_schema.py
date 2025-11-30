from pydantic import BaseModel

class TalkRequest(BaseModel):
    user_id: str # (추가) 누구와의 대화인지 알아야 하므로 이메일 등을 받음
    message: str