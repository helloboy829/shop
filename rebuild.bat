@echo off
chcp 65001
set JAVA_HOME=C:\Program Files\Java\jdk-1.8
set PATH=%JAVA_HOME%\bin;%PATH%
cd /d d:\codeidea\bishe3\dts-shop
call mvn clean install -DskipTests -U
echo Build completed!
pause
