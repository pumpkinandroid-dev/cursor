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

echo 编译WAR...
mvn -f backend/pom.xml -DskipTests clean package

echo 尝试部署到本地 Tomcat...
if defined CATALINA_HOME (
  echo 检测到 CATALINA_HOME=%CATALINA_HOME%
  echo 复制 WAR 到 Tomcat webapps 目录...
  copy /Y backend\target\grade-management-system-1.0.0.war "%CATALINA_HOME%\webapps\grade-management-system.war"
  echo 启动 Tomcat...
  call "%CATALINA_HOME%\bin\startup.bat"
) else (
  echo 未检测到 CATALINA_HOME 环境变量。
  echo 请将 backend\target\grade-management-system-1.0.0.war 部署到 Tomcat 的 webapps 目录后启动 Tomcat。
)

pause
