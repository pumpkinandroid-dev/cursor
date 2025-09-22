# 成绩管理系统

一个基于Java后端、JavaScript前端和SQLite数据库的成绩管理系统。

## 功能特性

- 学生成绩的增删改查
- 按学号、姓名、班级、学期查询
- 数据统计和分析
- 响应式Web界面

## 技术栈

- **后端**: Java + Spring Boot
- **前端**: HTML + CSS + JavaScript
- **数据库**: SQLite
- **构建工具**: Maven

## 项目结构

```
grade-management-system/
├── backend/                 # Java后端
│   ├── src/main/java/
│   │   └── com/grade/
│   │       ├── entity/      # 实体类
│   │       ├── dao/         # 数据访问层
│   │       ├── service/     # 业务逻辑层
│   │       ├── controller/  # 控制器层
│   │       └── GradeApplication.java
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── database/
│   │       └── init.sql
│   └── pom.xml
├── frontend/                # 前端
│   ├── index.html
│   ├── style.css
│   └── script.js
└── README.md
```

## 快速开始

### 1. 启动后端服务
```bash
cd backend
mvn spring-boot:run
```

### 2. 打开前端页面
在浏览器中打开 `frontend/index.html`

## 数据库表结构

- **students**: 学生信息表
- **grades**: 成绩表

## API接口

- `GET /api/students` - 获取所有学生
- `POST /api/students` - 添加学生
- `PUT /api/students/{id}` - 更新学生信息
- `DELETE /api/students/{id}` - 删除学生
- `GET /api/grades` - 获取所有成绩
- `POST /api/grades` - 添加成绩
- `PUT /api/grades/{id}` - 更新成绩
- `DELETE /api/grades/{id}` - 删除成绩
