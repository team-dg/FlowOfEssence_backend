# 빌드 스테이지
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /home/gradle/project

# 루트 빌드 파일 복사
COPY build.gradle settings.gradle /home/gradle/project/

# 공통 모듈 복사
COPY common-module /home/gradle/project/common-module

# 서비스 모듈 복사
COPY api-gateway-server /home/gradle/project/api-gateway-server

# 빌드 실행
RUN gradle :api-gateway-server:build --no-daemon

# 실행 스테이지
FROM openjdk:17-slim

# 애플리케이션 디렉토리 생성
WORKDIR /app

# 빌드 결과물 복사
COPY --from=build /home/gradle/project/api-gateway-server/build/libs/*.jar app.jar

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=dev

# 포트 노출
EXPOSE 6001

# 애플리케이션 실행
ENTRYPOINT ["java","-jar","/app/app.jar"]