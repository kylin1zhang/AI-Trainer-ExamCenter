@echo off
chcp 65001 >nul
echo 检查数据库状态...
echo.

echo === 检查test用户 ===
mysql -u root -pAsdf1234 exam_center -e "SELECT id, username, nickname, LEFT(password, 20) as password_preview, status FROM tb_user WHERE username='test';"

echo.
echo === 检查admin用户 ===
mysql -u root -pAsdf1234 exam_center -e "SELECT id, username, nickname, LEFT(password, 20) as password_preview, status FROM tb_user WHERE username='admin';"

echo.
echo === 所有用户列表 ===
mysql -u root -pAsdf1234 exam_center -e "SELECT id, username, nickname, status FROM tb_user;"

pause

