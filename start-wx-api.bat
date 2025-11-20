@echo off
chcp 65001
echo ========================================
echo    启动小程序API服务 (端口 8080)
echo ========================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-1.8
set PATH=%JAVA_HOME%\bin;%PATH%

echo 正在启动服务...
java -jar dts-wx-api/target/dts-wx-api-0.0.1-SNAPSHOT-exec.jar --spring.profiles.active=dev

pause
