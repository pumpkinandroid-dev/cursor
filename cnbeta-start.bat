@echo off
setlocal
cd /d %~dp0

echo Checking Java...
java -version >nul 2>&1 || (echo Java not found. Please install Java 17+ & exit /b 1)

echo Checking Maven...
echo mvn -version >nul 2>&1 || (echo Maven not found. Please install Maven & exit /b 1)

echo Building cnbeta-summary...
mvn  -f cnbeta-summary/pom.xml clean package
if %errorlevel% neq 0 (
  echo Build failed.
  exit /b 1
)

set PORT=%PORT%
if "%PORT%"=="" set PORT=8080
echo Running on http://localhost:%PORT%
java -DPORT=%PORT% -jar cnbeta-summary\target\cnbeta-summary-1.0.0.jar




