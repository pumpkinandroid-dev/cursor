#!/bin/bash

echo "启动成绩管理系统..."
echo

# 检查Java环境
echo "检查Java环境..."
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境，请先安装Java 11或更高版本"
    exit 1
fi

# 检查Maven环境
echo "检查Maven环境..."
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven环境，请先安装Maven"
    exit 1
fi

# 进入后端目录
echo "进入后端目录..."
cd backend

# 编译 WAR
echo "编译 WAR..."
mvn -f backend/pom.xml -DskipTests clean package

# 尝试部署到本地 Tomcat
if [ -n "$CATALINA_HOME" ]; then
  echo "检测到 CATALINA_HOME=$CATALINA_HOME"
  echo "复制 WAR 到 Tomcat webapps 目录..."
  cp -f backend/target/grade-management-system-1.0.0.war "$CATALINA_HOME/webapps/grade-management-system.war"
  echo "启动 Tomcat..."
  "$CATALINA_HOME/bin/startup.sh"
else
  echo "未检测到 CATALINA_HOME 环境变量。"
  echo "请将 backend/target/grade-management-system-1.0.0.war 部署到 Tomcat 的 webapps 目录后启动 Tomcat。"
fi
