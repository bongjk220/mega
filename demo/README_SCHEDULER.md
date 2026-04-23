# 매일 아침마다 Crawler 자동 실행 설정

## 1. Spring Boot 스케줄링 설정 완료

### 생성된 파일:
- `CrawlerScheduler.java`: 매일 아침 8시에 자동 실행
- `DemoApplication.java`: `@EnableScheduling` 추가됨

### 스케줄링 설정:
```java
@Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
```
- 매일 아침 8시 (한국 시간)에 실행
- `movieService.loadJson()` 호출

## 2. Render.com 배포 시 설정

### 환경변수 추가 (필요 시):
```
SCHEDULER_ENABLED=true
```

### 로그 확인:
- Render.com 로그에서 "=== 매일 아침 크롤러 실행 시작 ===" 메시지 확인
- 성공/실패 여부 로그 기록

## 3. 테스트 방법

### 로컬 테스트:
1. 애플리케이션 실행
2. 로그에서 스케줄러 등록 확인
3. 테스트용 코드 주석 해제하여 매분 실행 가능

### Render.com 테스트:
1. 배포 후 로그 확인
2. 아침 8시에 자동 실행되는지 확인

## 4. 주의사항

- Render.com 무료 플랜에서는 서버가 잠들 수 있음
- `RenderKeepAliveController`가 서버 활성화 유지
- 크롤링 실패 시 에러 로그만 기록하고 애플리케이션은 계속 실행

## 5. 수동 실행

필요시 `/load` 엔드포인트로 수동 실행 가능:
```
GET https://megatestkr.onrender.com/load
```
