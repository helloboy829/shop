@echo off
chcp 65001
echo ========================================
echo    DTS-SHOP 一键启动所有服务
echo ========================================
echo.
echo 将依次启动：
echo 1. 管理后台API (端口 8083)
echo 2. 小程序API (端口 8080)
echo 3. 管理前端UI (端口 9527)
echo.
echo 请确保已经编译过项目 (运行过 build.bat)
echo.
pause

echo.
echo [1/3] 启动管理后台API...
start "管理后台API-8083" cmd /k start-admin-api.bat

echo 等待5秒...
timeout /t 5 /nobreak >nul

echo.
echo [2/3] 启动小程序API...
start "小程序API-8080" cmd /k start-wx-api.bat

echo 等待5秒...
timeout /t 5 /nobreak >nul

echo.
echo [3/3] 启动管理前端UI...
start "管理前端UI-9527" cmd /k start-admin-ui.bat

echo.
echo ========================================
echo    所有服务启动完成！
echo ========================================
echo.
echo 访问地址：
echo - 管理后台：http://localhost:9527
echo - 小程序：用微信开发者工具打开 wx-mini-program 文件夹
echo.
echo 默认账号：admin123 / admin123
echo.
pause
