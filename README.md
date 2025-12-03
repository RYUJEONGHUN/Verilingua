🤖 VeriLingua: AI & Blockchain 기반 영어 퀘스트 플랫폼


기간: 25.11.17~25.12.02


"AI 튜터가 채점하고, 블록체인이 증명하다."

https://verilingua-front.vercel.app/

VeriLingua는 사용자가 AI와 대화하며 영어 퀘스트를 수행하면, AI가 실시간으로 채점하고 성취 결과(Level)를 위변조 불가능한 **SBT(Soulbound Token)**로 발급하여 영구적으로 공증하는 학습형 DApp입니다.

📸 프로젝트 미리보기

<img width="1358" height="609" alt="image" src="https://github.com/user-attachments/assets/31922468-6c0f-47f2-bcaa-368b6a64ff67" />
<img width="1527" height="843" alt="image" src="https://github.com/user-attachments/assets/470e6119-bee7-4490-935f-24ee37a9e603" />


🚀 핵심 기능 (Key Features)

1. 🧠 AI Judge (AI 심판)

OpenAI GPT-4를 활용하여 단순 정답 매칭이 아닌, 문맥과 의도를 파악하는 정교한 채점 시스템을 구축했습니다.

사용자의 답변에 대해 PASS/FAIL 판정뿐만 아니라, 구체적인 피드백과 교정을 실시간으로 제공합니다.

RAG (Retrieval-Augmented Generation)를 적용하여, 사용자의 과거 오답 기록을 바탕으로 개인화된 조언을 제공합니다.


2. ⛓️ Invisible Wallet (지갑 자동 생성)

Web3에 익숙하지 않은 사용자를 위해, Google OAuth 로그인 시 백그라운드에서 블록체인 지갑을 자동으로 생성합니다.

사용자는 복잡한 지갑 연동 과정 없이 서비스를 이용하며 자연스럽게 블록체인 자산을 소유하게 됩니다.


3. 🏆 SBT Reward & Gating (자격 증명)

퀘스트 완료 시 양도 불가능한 NFT(SBT)를 발행하여 사용자의 학습 성과를 영구적으로 공증하고 IPFS에 메타데이터를 저장하여 데이터의 탈중앙화와 영속성을 보장합니다.

상위 레벨 도전 시 하위 레벨 SBT 보유 여부를 검증하는 On-chain Gating System을 구현했습니다.


4. 🔒 Advanced Security & Architecture

MSA (Microservices Architecture): Spring Boot(Main)와 FastAPI(AI Worker)를 분리하여 확장성과 안정성을 확보했습니다.

JWT + Redis: Access Token과 Refresh Token(Redis 저장)을 활용한 안전하고 효율적인 인증 시스템을 구축했습니다.


🏛️ 시스템 아키텍처 (System Architecture)

<img width="932" height="670" alt="스크린샷 2025-12-01 170343" src="https://github.com/user-attachments/assets/b2999de4-b148-4eb8-a17a-8a29821d2964" />


🛠️ 기술 스택 (Tech Stack)

🧠 Backend (Main Server – Spring Boot)

Language & Framework

Java 17, Spring Boot

Security & Auth

Spring Security

OAuth 2.0 Client (Google 로그인)

JWT 기반 인증/인가

Database & Persistence

PostgreSQL

Spring Data JPA

Communication

Spring WebFlux WebClient (AI 서버, 블록체인 서버 연동)

Infrastructure

Docker / Docker Compose

Redis (세션/캐시, 토큰/로그 관리)

🤖 AI Server (Evaluation & Chat)

Language & Framework

Python 3.11

FastAPI

AI & LLM

OpenAI API (GPT-4o, Whisper)

LangChain 기반 프롬프트/체인 구성

Responsibilities

영어 답변 채점 (JSON 모드 심판)

스몰토크 / 튜터 모드 대화

사용자 히스토리 기반 맞춤 피드백

⛓️ Blockchain & Web3

Smart Contract

Solidity

ERC-721 URIStorage 기반 SBT/NFT 컨트랙트

Remix IDE 로 개발 및 배포

Blockchain Client

Web3.py (지갑 생성, 트랜잭션 서명/전송)

On-chain Data

SBT 발행, 트랜잭션 해시 관리

IPFS 연동을 통한 메타데이터 관리

🎨 Frontend (User Interface)

Framework & Language

React (Vite)

TypeScript

Styling & UI

Tailwind CSS

커스텀 다크 테마 대시보드 / 채팅 UI

State & Networking

React Context API

Axios Interceptor (JWT 자동 첨부 & 401 → 토큰 재발급 처리)

Auth Flow

Google OAuth2 로그인 버튼 → Spring Boot → JWT 콜백 처리

LocalStorage 기반 로그인 상태 유지

💾 Storage & Infra (공통)

Container

Docker, Docker Compose

Cache / Session / Logs

Redis

Metadata Storage

IPFS (SBT 메타데이터 JSON, 이미지 CID 관리)





