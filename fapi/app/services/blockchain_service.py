import json
import os
from web3 import Web3
from dotenv import load_dotenv

load_dotenv()

# 환경변수 로드
PRIVATE_KEY = os.getenv("WALLET_PRIVATE_KEY") # 배포자(Admin) 개인키
CONTRACT_ADDRESS = os.getenv("SBT_CONTRACT_ADDRESS")
RPC_URL = "https://1rpc.io/sepolia" # 또는 아까 쓴 1rpc 등

L1_TOKEN_URI = os.getenv("L1_TOKEN_URI") 

# ABI 로드 (파일 위치 주의: app/.. 경로에 맞게 수정)
with open("VeriLinguaSBT.json", "r") as f:
    contract_data = json.load(f)
    ABI = contract_data["abi"]

w3 = Web3(Web3.HTTPProvider(RPC_URL))
def create_wallet() -> dict:
    """
    새로운 이더리움/폴리곤 지갑을 생성합니다.
    """
    w3 = Web3()
    account = w3.eth.account.create()
    
    return {
        "address": account.address,
        "private_key": account._private_key.hex() 
        # 주의: 실무에서는 private_key를 절대 평문으로 리턴하거나 저장하면 안 됩니다! 
        # 하지만 MVP 포트폴리오용으로는 DB에 저장하고 암호화했다고 가정하거나,
        # 나중에 Spring에서 AES 암호화 후 저장하는 로직을 추가하면 됩니다.
    }

def mint_sbt(to_address: str) -> str:
    """
    지정된 주소(to_address)로 SBT를 발행하고 트랜잭션 해시를 반환합니다.
    """
    if not w3.is_connected():
        raise Exception("Blockchain connection failed")

    # 1. 컨트랙트 로드
    contract = w3.eth.contract(address=CONTRACT_ADDRESS, abi=ABI)
    
    # 2. 배포자 계정 (Admin)
    admin_account = w3.eth.account.from_key(PRIVATE_KEY)
    
    # 3. 트랜잭션 구성 (safeMint 함수 호출)
    # 주의: 가스비가 부족하면 실패하니 넉넉하게 설정
    nonce = w3.eth.get_transaction_count(admin_account.address)
    
     # 🔹 여기서 safeMint에 uri까지 같이 전달
    tx = contract.functions.safeMint(
        to_address,
        L1_TOKEN_URI
    ).build_transaction({
        "from": admin_account.address,
        "nonce": nonce,
        "gas": 500000,
        "gasPrice": w3.eth.gas_price,
    })
    
    # 4. 서명 및 전송
    signed_tx = w3.eth.account.sign_transaction(tx, PRIVATE_KEY)
    tx_hash = w3.eth.send_raw_transaction(signed_tx.raw_transaction)
    
    return tx_hash.hex() # 트랜잭션 해시(영수증 번호) 반환