from web3 import Web3
from dotenv import load_dotenv
import os
import json  # json 라이브러리 필수

# 1. 환경변수 로드
load_dotenv()
PRIVATE_KEY = os.getenv("WALLET_PRIVATE_KEY")
RPC_URL = "https://1rpc.io/sepolia"

# 🚀 [수정된 부분] JSON 파일에서 ABI와 Bytecode 읽어오기
def load_contract_data():
    with open("VeriLinguaSBT.json", "r", encoding="utf-8") as f:
        data = json.load(f)
        return data["abi"], data["bytecode"]

# 데이터를 변수에 담기
ABI, BYTECODE = load_contract_data()

def deploy_contract():
    # 3. 블록체인 연결
    w3 = Web3(Web3.HTTPProvider(RPC_URL))
    if not w3.is_connected():
        print("❌ 블록체인 연결 실패!")
        return

    print(f"📡 연결된 네트워크: {RPC_URL}")
    
    # 4. 배포자 계정 설정
    account = w3.eth.account.from_key(PRIVATE_KEY)
    print(f"👤 배포자 주소: {account.address}")

    # 5. 컨트랙트 객체 생성
    Contract = w3.eth.contract(abi=ABI, bytecode=BYTECODE)

    # 6. 배포 트랜잭션 생성
    print("🚀 컨트랙트 배포 중... (기다려주세요)")
    construct_txn = Contract.constructor().build_transaction({
        'from': account.address,
        'nonce': w3.eth.get_transaction_count(account.address),
        'gas': 5000000,
        'gasPrice': w3.eth.gas_price
    })

    # 7. 서명 및 전송
    signed = w3.eth.account.sign_transaction(construct_txn, private_key=PRIVATE_KEY)
    tx_hash = w3.eth.send_raw_transaction(signed.raw_transaction)
    
    print(f"⏳ 트랜잭션 전송됨: {tx_hash.hex()}")
    
    # 8. 완료 대기
    tx_receipt = w3.eth.wait_for_transaction_receipt(tx_hash)
    
    print("✅ 배포 완료!")
    print(f"📄 컨트랙트 주소: {tx_receipt.contractAddress}")
    print("👉 이 주소를 Spring Boot 설정 파일에 넣으세요!")

if __name__ == "__main__":
    deploy_contract()