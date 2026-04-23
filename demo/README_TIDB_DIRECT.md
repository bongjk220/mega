# TiDB Cloud 직접 저장/읽기 방식

## 개요

파일 시스템 의존성을 완전히 제거하고 TiDB Cloud를 직접 사용하는 방식으로 전환합니다.

## 동작 방식

```
크롤러(crawler_tidb.py) → TiDB Cloud 직접 저장
웹사이트(MovieService) → TiDB Cloud 직접 읽기
```

## 파일 수정 내용

### 1. crawler_tidb.py (신규 생성)
- 기존 crawler.py에서 파일 저장 부분 제거
- TiDB Cloud 직접 연결 및 저장
- HTML 엔티티 정제 기능 포함
- 테이블 자동 생성/재생성

### 2. MovieService.java (수정)
- JSON 파일 읽기 로직 제거
- TiDB Cloud에서 직접 데이터 읽기
- 불필요한 import 정리

## 실행 방법

### 1. Python 크롤러 실행
```bash
# 필요한 라이브러리 설치
pip install mysql-connector-python requests

# 크롤러 실행
python crawler/crawler_tidb.py
```

### 2. Spring Boot 애플리케이션 실행
```bash
./gradlew bootRun
```

## 스케줄러 설정

- 매일 아침 9시30분에 자동 실행
- `CrawlerScheduler.java`에서 `crawler_tidb.py` 호출 필요

## 장점

✅ **파일 시스템 불필요**: Render.com 권한 문제 해결
✅ **실시간 데이터**: 크롤링 즉시 반영
✅ **데이터 영속성**: TiDB Cloud에 영구 저장
✅ **중앙 관리**: 단일 데이터 소스

## TiDB Cloud 연결 정보

```python
TIDB_HOST = "gateway01.ap-northeast-1.prod.aws.tidbcloud.com"
TIDB_PORT = 4000
TIDB_USER = "PrkazshsERPiVWq.root"
TIDB_PASSWORD = "iQcYuElTAhN3wGD5"
TIDB_DATABASE = "mydb"
```

## 주의사항

1. **네트워크 연결**: 인터넷 연결 필요
2. **보안**: 연결 정보 환경변수로 관리 권장
3. **동시성**: 여러 크롤러 동시 실행 방지 필요

## 테스트 방법

1. `python crawler/crawler_tidb.py` 실행
2. Spring Boot 애플리케이션 시작
3. 웹사이트에서 데이터 확인
4. TiDB Cloud에서 데이터 직접 확인 가능
