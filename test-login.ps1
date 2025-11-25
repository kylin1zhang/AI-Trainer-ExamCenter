# 测试登录 API
$loginUrl = "http://localhost:8080/api/auth/login"
$registerUrl = "http://localhost:8080/api/auth/register"

Write-Host "=== 测试 1: 尝试注册新用户 ===" -ForegroundColor Cyan
$registerBody = @{
    username = "testuser"
    password = "123456"
    confirmPassword = "123456"
    nickname = "测试用户2"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri $registerUrl -Method POST -Body $registerBody -ContentType "application/json"
    Write-Host "注册成功！" -ForegroundColor Green
    Write-Host ($registerResponse | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "注册失败或用户已存在" -ForegroundColor Yellow
    Write-Host $_.Exception.Message
}

Write-Host "`n=== 测试 2: 使用新注册的用户登录 ===" -ForegroundColor Cyan
$loginBody = @{
    username = "testuser"
    password = "123456"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri $loginUrl -Method POST -Body $loginBody -ContentType "application/json"
    Write-Host "登录成功！" -ForegroundColor Green
    Write-Host ($loginResponse | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "登录失败" -ForegroundColor Red
    Write-Host $_.Exception.Message
}

Write-Host "`n=== 测试 3: 尝试使用 admin 登录 ===" -ForegroundColor Cyan
$adminBody = @{
    username = "admin"
    password = "123456"
} | ConvertTo-Json

try {
    $adminResponse = Invoke-RestMethod -Uri $loginUrl -Method POST -Body $adminBody -ContentType "application/json"
    Write-Host "admin 登录成功！" -ForegroundColor Green
    Write-Host ($adminResponse | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "admin 登录失败" -ForegroundColor Red
    Write-Host $_.Exception.Message
}

Write-Host "`n=== 测试 4: 尝试使用 test 登录 ===" -ForegroundColor Cyan
$testBody = @{
    username = "test"
    password = "123456"
} | ConvertTo-Json

try {
    $testResponse = Invoke-RestMethod -Uri $loginUrl -Method POST -Body $testBody -ContentType "application/json"
    Write-Host "test 登录成功！" -ForegroundColor Green
    Write-Host ($testResponse | ConvertTo-Json -Depth 3)
} catch {
    Write-Host "test 登录失败" -ForegroundColor Red
    Write-Host $_.Exception.Message
}

