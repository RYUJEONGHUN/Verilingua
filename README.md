🤖 VeriLingua: AI & Blockchain 기반 영어 퀘스트 플랫폼

<div align="center">
<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://www.google.com/search?q=https://img.shields.io/badge/FastAPI-009688%3Fstyle%3Dfor-the-badge%26logo%3Dfastapi%26logoColor%3Dwhite">
<img src="https://www.google.com/search?q=https://img.shields.io/badge/React-61DAFB%3Fstyle%3Dfor-the-badge%26logo%3Dreact%26logoColor%3Dblack">
<img src="https://www.google.com/search?q=https://img.shields.io/badge/Solidity-363636%3Fstyle%3Dfor-the-badge%26logo%3Dsolidity%26logoColor%3Dwhite">
<img src="https://www.google.com/search?q=https://img.shields.io/badge/OpenAI-412991%3Fstyle%3Dfor-the-badge%26logo%3Dopenai%26logoColor%3Dwhite">





<img src="https://www.google.com/search?q=https://img.shields.io/badge/Docker-2496ED%3Fstyle%3Dfor-the-badge%26logo%3Ddocker%26logoColor%3Dwhite">
<img src="https://www.google.com/search?q=https://img.shields.io/badge/PostgreSQL-4169E1%3Fstyle%3Dfor-the-badge%26logo%3Dpostgresql%26logoColor%3Dwhite">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
</div>

"AI 튜터가 채점하고, 블록체인이 증명하다."

VeriLingua는 사용자가 AI와 대화하며 영어 퀘스트를 수행하면, AI가 실시간으로 채점하고 성취 결과(Level)를 위변조 불가능한 **SBT(Soulbound Token)**로 발급하여 영구적으로 공증하는 학습형 DApp입니다.

📸 프로젝트 미리보기

(여기에 실제 실행 화면 스크린샷이나 GIF를 넣으세요. 예: ![Demo](./demo.gif))

🚀 핵심 기능 (Key Features)

1. 🧠 AI Judge (AI 심판)

OpenAI GPT-4를 활용하여 단순 정답 매칭이 아닌, 문맥과 의도를 파악하는 정교한 채점 시스템을 구축했습니다.

사용자의 답변에 대해 PASS/FAIL 판정뿐만 아니라, 구체적인 피드백과 교정을 실시간으로 제공합니다.

**RAG (Retrieval-Augmented Generation)**를 적용하여, 사용자의 과거 오답 기록을 바탕으로 개인화된 조언을 제공합니다.

2. ⛓️ Invisible Wallet (지갑 자동 생성)

Web3에 익숙하지 않은 사용자를 위해, Google OAuth 로그인 시 백그라운드에서 블록체인 지갑을 자동으로 생성합니다.

사용자는 복잡한 지갑 연동 과정 없이 서비스를 이용하며 자연스럽게 블록체인 자산을 소유하게 됩니다.

3. 🏆 SBT Reward & Gating (자격 증명)

퀘스트 완료 시 **양도 불가능한 NFT (SBT)**를 발행하여 사용자의 학습 성과를 영구적으로 공증합니다.

IPFS에 메타데이터를 저장하여 데이터의 탈중앙화와 영속성을 보장합니다.

상위 레벨 도전 시 하위 레벨 SBT 보유 여부를 검증하는 On-chain Gating System을 구현했습니다.

4. 🔒 Advanced Security & Architecture

MSA (Microservices Architecture): Spring Boot(Main)와 FastAPI(AI Worker)를 분리하여 확장성과 안정성을 확보했습니다.

JWT + Redis: Access Token과 Refresh Token(Redis 저장)을 활용한 안전하고 효율적인 인증 시스템을 구축했습니다.

🏛️ 시스템 아키텍처 (System Architecture)

graph TD
    User((User)) -->|Access| Frontend[React Frontend]
    
    subgraph "Docker Compose Network"
        Frontend -->|API Request| Spring[Spring Boot Server]
        
        Spring <-->|Auth Info| DB[(PostgreSQL)]
        Spring <-->|Token Cache| Redis[(Redis)]
        
        Spring -->|Request Judge/Mint| FastAPI[FastAPI AI Server]
        
        FastAPI -->|LLM Query| OpenAI[OpenAI API]
        FastAPI -->|Transaction| Blockchain[Polygon/Sepolia Network]
        FastAPI -->|Metadata| IPFS[IPFS Node/Pinata]
    end
    
    Blockchain -->|Verify| Etherscan[Block Explorer]


🛠️ 기술 스택 (Tech Stack)

Backend (Main Server)

Language: Java 17

Framework: Spring Boot 3.x

Security: Spring Security, OAuth 2.0 Client

Database: Spring Data JPA, PostgreSQL

Communication: Spring WebFlux (WebClient)

AI Server (Worker)

Language: Python 3.11

Framework: FastAPI

AI: OpenAI API (GPT-4o, Whisper), LangChain (Concept)

Blockchain: Web3.py

Frontend

Framework: React (Vite), TypeScript

Styling: Tailwind CSS

State: Context API, Axios Interceptor

Infrastructure & DevOps

Container: Docker, Docker Compose

Storage: Redis (Cache), IPFS (Metadata)

Smart Contract: Solidity (ERC-721 URIStorage), Remix IDE

🔧 설치 및 실행 (How to Run)

이 프로젝트는 Docker Compose를 사용하여 한 번의 명령어로 모든 서비스(Spring, FastAPI, DB, Redis)를 실행할 수 있습니다.

1. 환경 변수 설정 (.env)

프로젝트 루트의 각 폴더에 .env 파일을 생성하고 API 키를 입력하세요.

aiblockchainservice/.env

# Database
DB_HOST=postgres
DB_NAME=verilingua
DB_USER=postgres
DB_PASSWORD=your_password

# Google OAuth
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# Redis & AI Server URL
SPRING_DATA_REDIS_HOST=redis
FASTAPI_URL=http://verilingua-fastapi:8000


fapi/.env

# OpenAI
OPENAI_API_KEY=sk-proj-...

# Blockchain
WALLET_PRIVATE_KEY=0x... (Admin Wallet Private Key)
SBT_CONTRACT_ADDRESS=0x... (Deployed Contract Address)


2. 실행

프로젝트 루트 디렉토리에서 다음 명령어를 실행합니다.

docker-compose up --build


3. 접속

Frontend: http://localhost:5173

Backend API: http://localhost:8080

AI Server Docs: http://localhost:8000/docs

📂 디렉토리 구조 (Directory Structure)

verilingua-project
├── aiblockchainservice/    # Spring Boot (Main Server)
│   ├── src/main/java/...   # Domain-Driven Design (User, Quest, Wallet)
│   └── Dockerfile
├── fapi/                   # FastAPI (AI & Blockchain Worker)
│   ├── app/                # Services (OpenAI, Web3)
│   └── Dockerfile
├── verilingua-front/       # React (Frontend)
└── docker-compose.yml      # Orchestration Config


💡 프로젝트 회고 (Retrospective)

본 프로젝트는 **서로 다른 언어(Java, Python)와 기술(Web2, Web3, AI)**을 하나의 유기적인 서비스로 통합하는 데 중점을 두었습니다.

문제 해결: Spring Boot와 FastAPI 간의 통신 오버헤드와 데이터 직렬화 문제를 해결하기 위해 명확한 DTO 설계와 비동기 통신(WebFlux)을 도입했습니다.

UX 개선: 블록체인의 진입 장벽인 '지갑 생성'과 '가스비' 문제를 백엔드단에서 처리(Invisible Wallet)하여 Web2 사용자도 쉽게 접근할 수 있도록 설계했습니다.

확장성: MSA 구조를 채택하여 향후 AI 모델이 변경되거나 블록체인 네트워크가 변경되어도 메인 비즈니스 로직(Spring)에 영향을 주지 않도록 결합도를 낮췄습니다.

📧 Contact

Developer: 류정훈

Email: jung.hun.ryu4@gmail.com

GitHub:
