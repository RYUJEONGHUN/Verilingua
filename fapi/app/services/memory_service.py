import redis.asyncio as redis
import json
import os

# Redis 연결
redis_client = redis.from_url(
    url="redis://verilingua-redis:6379", # 도커 서비스 이름
    decode_responses=True
)

async def add_message_to_history(user_id: str, role: str, content: str):
    """
    대화 내용을 Redis에 저장합니다. (List)
    key: "chat:{user_id}"
    value: {"role": "user", "content": "..."}
    """
    message = json.dumps({"role": role, "content": content})
    key = f"chat:{user_id}"
    
    # 리스트 오른쪽에 추가 (RPUSH)
    await redis_client.rpush(key, message)
    
    # (선택) 너무 길어지면 오래된 것 삭제 (최근 20개만 유지)
    await redis_client.ltrim(key, -20, -1)
    # (선택) 대화 내용 1일 뒤 자동 삭제
    await redis_client.expire(key, 86400) 

async def get_chat_history(user_id: str):
    """
    최근 대화 기록을 가져옵니다.
    """
    key = f"chat:{user_id}"
    # 리스트 전체 가져오기 (LRANGE)
    messages = await redis_client.lrange(key, 0, -1)
    
    return [json.loads(msg) for msg in messages]