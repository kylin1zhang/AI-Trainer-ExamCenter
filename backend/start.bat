@echo off
chcp 65001 >nul
echo ====================================
echo 启动 AI Trainer Exam Center 后端
echo ====================================
echo.

set JAVA_HOME=E:\Java\jdk-17.0.17
set PATH=%JAVA_HOME%\bin;%PATH%

echo 检查 Java 版本...
java -version
echo.

echo 启动 Spring Boot 应用...
cd /d "%~dp0"
call mvn spring-boot:run

pause

