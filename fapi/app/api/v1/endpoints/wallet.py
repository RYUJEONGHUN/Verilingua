from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from app.services.blockchain_service import create_wallet, mint_sbt # mint_sbt 추가


router = APIRouter()

class WalletResponse(BaseModel):
    address: str
    private_key: str

@router.post("/create", response_model=WalletResponse)
async def generate_wallet():
    try:
        wallet = create_wallet()
        return wallet
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    

class MintRequest(BaseModel):
    target_address: str

@router.post("/mint")
async def issue_sbt(request: MintRequest):
    try:
        tx_hash = mint_sbt(request.target_address)
        return {"status": "success", "tx_hash": tx_hash}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))