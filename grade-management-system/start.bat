@echo off
echo 启动成绩管理系统...
echo.

echo 检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请先安装Java 11或更高版本
    pause
    exit /b 1
)

echo 检查Maven环境...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven环境，请先安装Maven
    pause
    exit /b 1
)

echo 进入后端目录...
cd backend

echo 编译并通过 Jetty 启动后端服务...
mvn clean package
mvn -f backend/pom.xml jetty:run

pause
