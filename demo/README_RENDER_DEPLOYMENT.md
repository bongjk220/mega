# Render.com 배포 데이터 적재 문제 해결

## 문제 원인

1. **파일 경로 문제**
   - Render.com에서는 `data/movies.json` 파일을 찾을 수 없음
   - Render.com 파일 시스템은 임시 디렉토리 사용

2. **환경 변수 미설정**
   - `RENDER_DATA_DIR` 환경변수가 설정되지 않음
   - 로컬과 Render.com 경로가 다름

## 해결 방안

### 1. ClassPath 리소스 사용 (권장)

Render.com 파일 시스템 권한 제한 문제로 ClassPath 리소스 사용:

- `src/main/resources/movies.json` 파일 위치
- Spring의 `ClassPathResource`로 파일 읽기
- 파일 시스템 접근 없이 리소스 로드

### 2. JSON 파일 위치 변경

`movies.json` 파일을 프로젝트 리소스 폴더로 이동:

```bash
# 프로젝트 루트에서 실행
mkdir -p src/main/resources
cp data/movies.json src/main/resources/
git add src/main/resources/movies.json
git commit -m "Move movies.json to resources for Render.com"
git push
```

### 3. MovieService.java 수정 완료

- 파일 시스템 접근 대신 ClassPath 리소스 사용
- 환경변수 불필요
- Render.com 완벽 호환

## 테스트 방법

### 1. 로컬 테스트
```bash
# 환경변수 없이 실행 (로컬)
./gradlew bootRun

# 환경변수 설정하여 실행 (Render.com 시뮬레이션)
RENDER_DATA_DIR=/opt/render/project/src/main/resources ./gradlew bootRun
```

### 2. Render.com 로그 확인

1. Render.com 대시보드 → Logs 탭
2. `=== MovieService.loadJson() 시작 ===` 메시지 확인
3. 파일 경로 로그 확인
4. JSON 읽기 성공/실패 로그 확인

## 예상 로그 출력

### 성공 시:
```
=== MovieService.loadJson() 시작 ===
Render.com 환경: /opt/render/project/src/main/resources/movies.json
파일 존재 여부: true
JSON 파일 읽기 시도...
읽은 영화 수: 10
데이터베이스 저장 완료: 10개
```

### 실패 시:
```
=== MovieService.loadJson() 시작 ===
로컬 환경: data/movies.json
파일 존재 여부: false
JSON 파일 없음 - 샘플 데이터 생성
```

## 주의사항

1. **권한 문제**: Render.com에서는 파일 쓰기 권한이 제한될 수 있음
2. **데이터 지속성**: Render.com 무료 플랜에서는 재배포 시 데이터 초기화될 수 있음
3. **대안**: 영구적인 데이터 저장소(TiDB Cloud) 사용 권장

## 최종 해결책

가장 좋은 해결책은 **TiDB Cloud 연결**입니다:

1. Render.com 환경변수에 TiDB Cloud 정보 설정
2. 스케줄러가 자동으로 크롤링하여 TiDB Cloud에 저장
3. 파일 시스템 의존성 제거

이렇게 하면 재배포해도 데이터가 유지됩니다.
