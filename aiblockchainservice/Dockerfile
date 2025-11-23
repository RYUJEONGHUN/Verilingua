# 1. 베이스 이미지 (Java 17 실행 환경)
FROM eclipse-temurin:17-jdk-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일을 도커 내부로 복사
# (주의: 빌드 후 생성되는 jar 파일 경로를 맞춰야 함)
COPY build/libs/*-SNAPSHOT.jar app.jar

# 4. 컨테이너 실행 시 실행할 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]