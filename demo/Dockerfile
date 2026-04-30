# 1단계: 빌드
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .

# gradlew 대신 시스템 설치된 gradle 명령어로 빌드 (Wrapper 파일 필요 없음)
RUN gradle build -x test

# 2단계: 실행
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Python 설치 (크롤러 실행 필요)
RUN apt-get update && apt-get install -y python3 python3-pip && \
    pip3 install mysql-connector-python requests && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Python 심볼릭 링크 생성
RUN ln -s /usr/bin/python3 /usr/bin/python

# build/libs 폴더 안의 jar 파일을 가져옴
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

# 크롤러 파일 복사
COPY crawler /app/crawler

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]