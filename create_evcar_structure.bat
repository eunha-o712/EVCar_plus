REM =====================================================
REM [용어 설명]
REM =====================================================
REM  REM         = 주석. Java의 // 와 같음. 실행 안 되고 메모용
REM  @echo off   = 명령어 실행 과정을 화면에 안 보여줌 (깔끔하게)
REM  echo        = 화면에 메시지 출력. Java의 System.out.println() 과 같음
REM  chcp 65001  = 한글이 깨지지 않게 인코딩을 UTF-8로 설정
REM  >nul        = 출력 메시지를 숨김 (결과를 화면에 안 보여줌)
REM  2>nul       = 에러 메시지를 숨김 (이미 폴더가 있어도 에러 안 뜸)
REM  mkdir       = make directory. 폴더를 만드는 명령어
REM  pause       = "아무 키나 누르세요" 멈춤. 결과 확인용
REM ================================================================
REM [사용법]
REM  1. 이 파일을 EVCar 프로젝트 루트 폴더에 넣기
REM     (src 폴더, build.gradle 이 보이는 그 위치)
REM  2. 더블클릭
REM  3. STS에서 프로젝트 우클릭 → Refresh (F5)
REM ================================================================

@echo off
chcp 65001>nul

echo ===== EVCar 프로젝트 폴더 구조 생성 시작 =====
echo.

REM - JAVA 패키지 (Domain)

mkdir "src\main\java\com\evcar\domain\user"2>nul
mkdir "src\main\java\com\evcar\domain\vehicle"2>nul
mkdir "src\main\java\com\evcar\domain\wishlist"2>nul
mkdir "src\main\java\com\evcar\domain\consultation"2>nul
mkdir "src\main\java\com\evcar\domain\inquiry"2>nul
mkdir "src\main\java\com\evcar\domain\faq"2>nul
mkdir "src\main\java\com\evcar\domain\chatbot"2>nul
mkdir "src\main\java\com\evcar\domain\charging"2>nul
mkdir "src\main\java\com\evcar\domain\mypage"2>nul

REM - JAVA 패키지 (Repository)

mkdir "src\main\java\com\evcar\repository\user"2>nul
mkdir "src\main\java\com\evcar\repository\vehicle"2>nul
mkdir "src\main\java\com\evcar\repository\wishlist"2>nul
mkdir "src\main\java\com\evcar\repository\consultation"2>nul
mkdir "src\main\java\com\evcar\repository\inquiry"2>nul
mkdir "src\main\java\com\evcar\repository\faq"2>nul
mkdir "src\main\java\com\evcar\repository\chatbot"2>nul
mkdir "src\main\java\com\evcar\repository\charging"2>nul

REM - JAVA 패키지 (Service)

mkdir "src\main\java\com\evcar\service\user"2>nul
mkdir "src\main\java\com\evcar\service\login"2>nul
mkdir "src\main\java\com\evcar\service\mypage"2>nul
mkdir "src\main\java\com\evcar\service\vehicle"2>nul
mkdir "src\main\java\com\evcar\service\wishlist"2>nul
mkdir "src\main\java\com\evcar\service\consultation"2>nul
mkdir "src\main\java\com\evcar\service\inquiry"2>nul
mkdir "src\main\java\com\evcar\service\faq"2>nul
mkdir "src\main\java\com\evcar\service\chatbot"2>nul
mkdir "src\main\java\com\evcar\service\charging"2>nul
mkdir "src\main\java\com\evcar\service\admin"2>nul

REM - JAVA 패키지 (Controller)

mkdir "src\main\java\com\evcar\controller"2>nul
mkdir "src\main\java\com\evcar\controller\user"2>nul
mkdir "src\main\java\com\evcar\controller\login"2>nul
mkdir "src\main\java\com\evcar\controller\mypage"2>nul
mkdir "src\main\java\com\evcar\controller\vehicle"2>nul
mkdir "src\main\java\com\evcar\controller\wishlist"2>nul
mkdir "src\main\java\com\evcar\controller\consultation"2>nul
mkdir "src\main\java\com\evcar\controller\inquiry"2>nul
mkdir "src\main\java\com\evcar\controller\faq"2>nul
mkdir "src\main\java\com\evcar\controller\chatbot"2>nul
mkdir "src\main\java\com\evcar\controller\charging"2>nul
mkdir "src\main\java\com\evcar\controller\admin"2>nul

REM - JAVA 패키지 (dto)

mkdir "src\main\java\com\evcar\dto\user"2>nul
mkdir "src\main\java\com\evcar\dto\login"2>nul
mkdir "src\main\java\com\evcar\dto\mypage"2>nul
mkdir "src\main\java\com\evcar\dto\vehicle"2>nul
mkdir "src\main\java\com\evcar\dto\consultation"2>nul
mkdir "src\main\java\com\evcar\dto\inquiry"2>nul
mkdir "src\main\java\com\evcar\dto\chatbot"2>nul
mkdir "src\main\java\com\evcar\dto\charging"2>nul
mkdir "src\main\java\com\evcar\dto\admin"2>nul

REM - JAVA 패키지 (공통)

mkdir "src\main\java\com\evcar\config"2>nul
mkdir "src\main\java\com\evcar\config\user"2>nul
mkdir "src\main\java\com\evcar\common\vo"2>nul
mkdir "src\main\java\com\evcar\common\util"2>nul
mkdir "src\main\java\com\evcar\interceptor"2>nul
mkdir "src\main\java\com\evcar\exception"2>nul
mkdir "src\main\java\com\evcar\validator\mypage"2>nul
mkdir "src\main\java\com\evcar\mapper\charging"2>nul
mkdir "src\main\java\com\evcar\api\charging"2>nul

REM - templates (resources)

mkdir "src\main\resources\templates\layout"2>nul
mkdir "src\main\resources\templates\fragments"2>nul
mkdir "src\main\resources\templates\error"2>nul
mkdir "src\main\resources\templates\user"2>nul
mkdir "src\main\resources\templates\login"2>nul
mkdir "src\main\resources\templates\mypage"2>nul
mkdir "src\main\resources\templates\vehicle"2>nul
mkdir "src\main\resources\templates\wishlist"2>nul
mkdir "src\main\resources\templates\consultation"2>nul
mkdir "src\main\resources\templates\inquiry"2>nul
mkdir "src\main\resources\templates\faq"2>nul
mkdir "src\main\resources\templates\charging"2>nul
mkdir "src\main\resources\templates\admin\user"2>nul
mkdir "src\main\resources\templates\admin\vehicle"2>nul
mkdir "src\main\resources\templates\admin\consultation"2>nul
mkdir "src\main\resources\templates\admin\inquiry"2>nul
mkdir "src\main\resources\templates\admin\faq"2>nul

REM - static resources (CSS, JS, Image)

mkdir "src\main\resources\static\css"2>nul
mkdir "src\main\resources\static\js"2>nul
mkdir "src\main\resources\static\images"2>nul

echo.
echo ===== 생성 완료 =====
echo.
echo 폴더에서 .bat 파일 실행 후, STS에서 프로젝트 우클릭 → Refresh 하면 폴더가 보입니다.
echo.
pause