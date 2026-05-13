# readme.md
# 소스를 GitHub배포하면 자동 배포 및 빌드됨

# Render에 아침8시에 깨워줌
- GitHub Actions (외부에서 깨움)
- .github/workflows/demo-keep-alive-deploy.yml
- .github/workflows/daDream-keep-alive-deploy.yml
# 이후 14분마다 자가핑 해줘서 잠들지 않게 해줌
- RenderKeepAliveController 
# megaTestkr의 경우 Python실행하여 9시에 DB에 적재하도록 스케쥴됨
- CrawlerScheduler -> crawlerService.runCrawler() -> crawler.py -> mydb에 movies테이블에 적재
# megaTestkr의 경우 재기동시에도 DB에 적재하도록 스케쥴됨
- DemoApplication.main() -> SpringApplication.run() -> ApplicationRunner.init() -> crawlerService.runCrawler() -> crawler.py -> crawler.py -> mydb에 movies테이블에 적재

# Render 기동시간
- megaTestkr : 08:00~19:00 = 11시간/일 × 30일 = 330시간
- daDream    : 08:00~18:00 = 10시간/일 × 30일 = 300시간
- 합계 = 630시간

# Render.com 세팅
- Render.com 가입
- 프로젝트 생성
- 서비스 생성
    - 주의: 서비스 이름 적을때 기존 이름이 있으면 이상한 이름이 생성됨
    - 루트디렉토리가 아니라면 루트디렉토리를 기재
    - Free로 가입
- 서비스 Settings
    - Auto-Deploy는 Off(하나만 쓸꺼면 OnCommit으로 해도 됨)
    - Health Checks에 /health-check 저장
- 서비스 Environment
    - 변수선언할것 저장

# GitHub Actions 세팅
- GitHub 가입
- GitHub에 repositories를 만들고 소스를 push
- Settings > Actions > General > Workflow permissions > Read and write permissions를 선택하여 체크한다.
- Settings > secrets and variables > Actions > Repository secrets 에 Render의 서비스 Settings > Deploy Hook 정보를 카피하여 저장

# GitHub Secrets 등록 목록
- DADREAM_DEPLOY_HOOK : Render daDream 서비스 Deploy Hook URL
- DEMO_DEPLOY_HOOK : Render megaTestkr 서비스 Deploy Hook URL

# 주의사항
- 워크플로어 .github\workflows 위치는 루트에 해야한다.
- 워크플로어 .yml파일의 name은 GitHub Actions 왼쪽에 보인다.
- .env 파일은 .gitignore에 등록하여 GitHub에 올라가지 않도록 할것
- application.properties/yml에 ID/패스워드 하드코딩 금지, 환경변수로 처리
- GitHub Webhooks에 Render Deploy Hook URL 등록 금지 (push마다 모든 서비스 배포됨)

