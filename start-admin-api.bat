@echo off
chcp 65001
echo ========================================
echo    启动管理后台API服务 (端口 8083)
echo ========================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-1.8
set PATH=%JAVA_HOME%\bin;%PATH%

echo 正在启动服务...
java -jar dts-admin-api/target/dts-admin-api-0.0.1-SNAPSHOT-exec.jar --spring.profiles.active=dev

pause
