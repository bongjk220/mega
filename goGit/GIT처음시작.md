# git 연결

▶ 만든 폴더를 git 연결
1. (탐색기) 폴더를 만든다. 만든폴더 오른쪽 마우스로 Code로 열기를 한다.
2. (VS Code) 왼쪽 세번째 소스제어(Crtl+Shift+G)를 눌러서 VS에서 초기화(Initailize Repository - git init)를 한다.
3. (WEB) github.com을 로그인한다. 
4. (VS Code) 변경내용 > ... > 원격 > 원격추가 > GitHub에서 원격추가 > 
5. (VS Code) (선택) git 목록을 선택
6. (VS Code) origin이라는 이름을 부여
7. (VS Code) 왼쪽아래 main을 클릭하면 repository 목록이 뜨는데 origin/main을 선택
8. (VS Code) 그러면 main을 checkout함

▶ 만든 폴더를 git 연결(CLI)
1. (탐색기) 폴더를 만든다. 만든폴더 오른쪽 마우스로 Code로 열기를 한다.
2. (VS Code) 왼쪽 빈공간에서 오른쪽 마우스로 터미널 열기
3. (터미널) git init
4. (터미널) git remote add origin https://github.com/fsclass-n/mega.git
5. (터미널) git fetch origin
6. (터미널) git checkout develop
7. (터미널) git branch -a  (브랜치 확인)
8. (터미널) git checkout -b feature/jaegeun
9. (터미널) git push --set-upstream origin feature/jaegeun
10. (VS Code) 파일수정후 
11. (터미널) git add .
12. (터미널) git commit -m "[재근] 수정 파일 작성 완료"
13. (터미널) git push
13. (터미널) git push
14. 