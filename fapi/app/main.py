from fastapi import FastAPI

from app.api.v1.endpoints import chat, wallet

app = FastAPI(title="VeriLingua AI Worker")

# 라우터 등록
app.include_router(chat.router, prefix="/api/v1/ai", tags=["AI"])
app.include_router(wallet.router, prefix="/api/v1/blockchain", tags=["Blockchain"]) # 추가

@app.get("/")
def health_check():
    return {"status": "AI Server is Alive! 🤖"}