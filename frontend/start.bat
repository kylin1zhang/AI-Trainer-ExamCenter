@echo off
chcp 65001 >nul
echo ====================================
echo 启动 AI Trainer Exam Center 前端
echo ====================================
echo.

cd /d "%~dp0"
echo 启动 Vite 开发服务器...
call npm run dev

pause

