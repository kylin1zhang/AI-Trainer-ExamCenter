@echo off
chcp 65001 >nul
echo ====================================
echo 导入测试用户到数据库
echo ====================================
echo.

echo 正在导入测试用户（admin 和 test）...
echo 密码：123456
echo.

cd /d "%~dp0"
mysql -u root -p exam_center --default-character-set=utf8mb4 < src\main\resources\sql\init_user.sql

echo.
echo ====================================
echo 导入完成！
echo ====================================
echo.
echo 测试账号信息：
echo   账号1: admin / 123456
echo   账号2: test / 123456
echo.

pause

