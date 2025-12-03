# 1) JDK 이미지
FROM eclipse-temurin:17-jdk

# 2) 작업 디렉토리
WORKDIR /app

# 3) Gradle 빌드 결과 copy
COPY build/libs/*.jar app.jar

# 4) 외부에서 접속할 포트
EXPOSE 8090

# 5) 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
