import redis.asyncio as redis
import os

# Redis 연결 풀 생성 (비동기)
redis_pool = redis.ConnectionPool.from_url(
    url="redis://redis:6379", # 도커 사용 시 "redis://redis:6379"
    decode_responses=True         # 문자열로 자동 변환
)

# 의존성 주입용 함수
async def get_redis():
    client = redis.Redis(connection_pool=redis_pool)
    try:
        yield client
    finally:
        await client.close()