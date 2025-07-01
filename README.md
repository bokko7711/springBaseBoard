spring 프레임워크를 이용해 구현한 게시판입니다. 
회원가입, 로그인, 회원정보 CRUD, 게시판 글 CRUD+검색, 댓글 CRUD, 파일 업로드/다운로드 등의 기능을 제공하고 있습니다.

실행 방법 : 
1. git clone을 통해, 프로젝트 파일을 복사해 가져옵니다.
2. intellij에서 '열기' => 복사한 파일 - build.gradle 선택 후 프로젝트로 열기를 선택합니다.
3. src/main/resources/에 application.properties 파일을 추가하고, 다음과 같이 입력합니다.

   spring.application.name=base

   spring.datasource.url= jdbc:h2:tcp://localhost/~/base

   spring.datasource.username=sa

   spring.jpa.hibernate.ddl-auto=update

   file.dir=(서버에 업로드된 파일이 저장될 경로)
   #ex) /Users/사용자이름/Desktop/temporaryFolder 

4. intellij의 외부 라이브러리 화면에서, h2 라이브러리의 버전을 확인합니다.
5. h2 database engine 홈페이지에 접속해서, 라이브러리 버전과 같은 버전의 database 파일을 다운받습니다.
6. iterm에 접속해서, h2/bin으로 이동합니다.
7. [chmod 755 h2.sh] 명령을 통해 권한을 부여해 줍니다. window의 경우 명령어가 다를 수 있습니다.
8. [./h2.sh] 명령 => 웹 브라우저에 h2 콘솔화면이 나타납니다.
9. jdbc url에 jdbc:h2:~/base를 입력하고, 연결 시험을 눌러 잘 되는지 확인 후 연결해서 db에 접속합니다.
10. 다른 iterm 탭을 열고, 유저 홈 디렉토리에서 base.mv.db파일이 있는지 확인합니다.
11. 이번엔 h2 콘솔에서 jdbc url에 jdbc:h2:tcp://localhost/~/base를 입력하고, 연결해서 db에 접속합니다.
12. intellijdptj BaseApplication을 Run : 프로젝트를 실행합니다.
13. localhost:8080/에 접속합니다.