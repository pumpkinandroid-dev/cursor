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

# 编译并启动后端服务
echo "编译并启动后端服务..."
mvn clean spring-boot:run
