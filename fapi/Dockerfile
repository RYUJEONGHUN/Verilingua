# 1. 파이썬 3.9 슬림 버전 (가볍고 안정적)
FROM python:3.11-slim

# 2. 작업 폴더 설정
WORKDIR /app

RUN pip install --upgrade pip

# 3. 라이브러리 목록 복사 및 설치 (캐싱 효율을 위해 코드보다 먼저 함)
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 4. 소스 코드 전체 복사
COPY . .

# 5. 서버 실행 (중요: host를 0.0.0.0으로 해야 외부에서 접속 가능!)
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]