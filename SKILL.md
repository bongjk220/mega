# 🛠️ mega 프로젝트 에이전트 스킬 명세서 (SKILL.md)

이 문서는 **mega** 프로젝트 내에서 AI 에이전트가 완벽하게 발휘하고 준수해야 하는 **기술 스택별 스킬(Skills) 및 명령어 가이드**입니다. 에이전트는 본 명세서의 지침에 근거하여 개발을 지원합니다.

---

## 🚀 1. 백엔드 개발 스킬 (Java & Spring Boot 4.0.5)

에이전트는 안정적이고 모던한 스프링 부트 애플리케이션 개발 스킬을 보유하고 있습니다.

### 💡 주요 스킬
*   **Layered Architecture 준수**: Controller - Service - Repository / Mapper 구조를 철저히 지킵니다.
*   **No Raw SQL & DB Safety**: TiDB(MySQL 호환) 접근 시 JPA 혹은 MyBatis Mapper를 사용하며, 파라미터 바인딩을 적용해 SQL Injection을 방지합니다.
*   **Validation**: `@Valid` 및 Bean Validation을 활용하여 입력을 확실히 검증합니다.
*   **Global Search Optimization**: 에디터 내에서 중복 없이 쉽게 검색될 수 있도록 클래스 및 주요 메소드 이름에 명확하고 고유한 네이밍(Unique Prefix)을 적용합니다.

### 💻 실행 및 테스트 명령어 (in `shop` directory)
```powershell
# 백엔드 서버 로컬 기동
./gradlew bootRun

# 백엔드 테스트 코드 실행
./gradlew test

# DB 마이그레이션 실행 (Flyway 필요 시)
./gradlew flywayMigrate
```

---

## 🐍 2. Python RPA 및 데이터 크롤러 개발 스킬

에이전트는 데이터 수집 자동화 및 스케줄링 처리를 위한 Python 개발 능력을 제공합니다.

### 💡 주요 스킬
*   **결측 데이터 엄격 처리**: 키워드, 가격, 재고 등의 원천 데이터 누락 시 추정하여 대입하지 않고, 명시적 오류(Exception)를 반환해 상위 레이어에 전달합니다.
*   **DB 적재 스케줄링**: `DemoApplication` 기동 시의 `ApplicationRunner.init()` 및 `CrawlerScheduler`와 연계된 `crawler.py` 호출 로직을 매끄럽게 처리합니다.

### 💻 실행 및 테스트 명령어 (in `shop` directory)
```powershell
# Python RPA 스크립트 실행
python rpa/main.py

# pytest를 이용한 Python 테스트 실행
pytest rpa/tests/ -q
```

---

## 🎨 3. 프론트엔드 및 UI/UX 구현 스킬 (Vanilla CSS, Modern HTML)

에이전트는 미적으로 아름답고 프리미엄한 프론트엔드 컴포넌트를 설계하고 개발할 수 있습니다.

### 💡 주요 스킬
*   **Premium Design**: generic한 브라우저 기본 컬러 대신, HSL 조율 컬러와 Harmonious Dark Mode, 그라데이션, 미세한 마이크로 애니메이션을 적용해 뛰어난 사용자 경험을 제공합니다.
*   **Design System 준수**: `shop/docs/DESIGN.md`가 있을 경우, 해당 파일에 정의된 인터(Inter) 폰트 규격, 8px 그리드, 화이트스페이스 비율을 100% 준수합니다.
*   **SEO & Accessibility**: 올바른 HTML5 시맨틱 마크업, 1페이지 1개의 `<h1>` 태그 사용, 요소별 고유 ID 부여 등을 적용합니다.

### 💻 프론트엔드 확인 방법
*   백엔드 기동 후 `http://localhost:8080/login.html` 접속 또는 지정된 정적 웹 페이지를 통해 확인합니다.

---

## ☁️ 4. GitHub Actions & 배포 자동화 스킬

에이전트는 CI/CD 파이프라인 관리 및 클라우드 배포 설정을 최적화할 수 있습니다.

### 💡 주요 스킬
*   **GitHub Actions Workflow 최적화**: `.github/workflows/` 내의 `.yml` 파일을 생성 및 튜닝합니다.
*   **자가 핑(Keep-Alive) 관리**: Render.com Free 티어의 수면 모드를 방지하기 위해 정해진 일정(예: 아침 8시 기동 후 14분 주기 자가 핑)을 관리하는 스케줄러 컨트롤러를 유지 관리합니다.
*   **보안 토큰 주입**: GitHub Secrets (`DEMO_DEPLOY_HOOK`, `DADREAM_DEPLOY_HOOK`) 등 민감 정보를 안전하게 연동합니다.
