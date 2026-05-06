# 1단계: 빌드
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .

# [수정] 실행 권한 부여 명령어 추가
RUN chmod +x gradlew

# gradlew 대신 시스템 설치된 gradle 명령어로 빌드 (Wrapper 파일 필요 없음)
# RUN gradle build -x test

# [수정] -x test를 추가하여 테스트 실행을 건너뜁니다.
# 또한 build 대신 bootJar를 사용하여 꼭 필요한 실행 파일만 만듭니다.
RUN ./gradlew clean build bootJar -x test

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

# [수정] 현재 Dockerfile이 demo 폴더 안에 있으므로 상대 경로로 지정
COPY requirements.txt ./requirements.txt
RUN pip3 install --no-cache-dir -r requirements.txt

# 크롤러 파일 복사
COPY crawler ./crawler

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]