# 成绩管理系统安装指南

## 系统要求

- Java 11 或更高版本
- Maven 3.6 或更高版本
- 现代浏览器（Chrome、Firefox、Safari、Edge）

## 安装步骤

### 1. 克隆或下载项目

```bash
git clone <项目地址>
cd grade-management-system
```

### 2. 检查环境

#### 检查Java版本
```bash
java -version
```

#### 检查Maven版本
```bash
mvn -version
```

### 3. 启动后端服务

#### Windows系统
```bash
start.bat
```

#### Linux/Mac系统
```bash
chmod +x start.sh
./start.sh
```

#### 手动启动
```bash
cd backend
mvn clean spring-boot:run
```

### 4. 访问前端页面

后端服务启动后，在浏览器中打开：
```
file:///path/to/grade-management-system/frontend/index.html
```

或者使用本地服务器：
```bash
cd frontend
python -m http.server 8000
```
然后访问：http://localhost:8000

## 功能说明

### 学生管理
- 添加学生信息（学号、姓名、班级）
- 编辑学生信息
- 删除学生（同时删除相关成绩）
- 搜索学生（按学号、姓名、班级）

### 成绩管理
- 添加成绩记录（学号、科目、成绩、学期、考试类型）
- 编辑成绩记录
- 删除成绩记录
- 按学生、科目、学期筛选成绩
- 搜索成绩

### 统计分析
- 学生总数统计
- 成绩记录总数
- 平均分、最高分统计
- 班级成绩分布图表
- 科目成绩分布图表

## 数据库

系统使用SQLite数据库，数据库文件会自动创建在项目根目录下：
- 文件名：`grade_management.db`
- 包含示例数据，可直接使用

## API接口

后端服务运行在 `http://localhost:8080`，提供RESTful API：

### 学生相关接口
- `GET /api/students` - 获取所有学生
- `POST /api/students` - 添加学生
- `PUT /api/students/{id}` - 更新学生
- `DELETE /api/students/{id}` - 删除学生
- `GET /api/students/search?keyword={keyword}` - 搜索学生

### 成绩相关接口
- `GET /api/grades` - 获取所有成绩
- `POST /api/grades` - 添加成绩
- `PUT /api/grades/{id}` - 更新成绩
- `DELETE /api/grades/{id}` - 删除成绩
- `GET /api/grades/search?keyword={keyword}` - 搜索成绩

## 故障排除

### 1. 端口被占用
如果8080端口被占用，可以修改 `backend/src/main/resources/application.properties` 中的端口配置：
```properties
server.port=8081
```

### 2. 数据库连接问题
确保项目目录有写入权限，SQLite数据库文件需要创建在项目目录下。

### 3. 前端无法连接后端
检查后端服务是否正常启动，确认API_BASE_URL配置正确。

### 4. 跨域问题
后端已配置CORS，支持跨域访问。如果仍有问题，检查浏览器控制台错误信息。

## 开发说明

### 项目结构
```
grade-management-system/
├── backend/                 # Java后端
│   ├── src/main/java/
│   │   └── com/grade/
│   │       ├── entity/      # 实体类
│   │       ├── dao/         # 数据访问层
│   │       ├── service/     # 业务逻辑层
│   │       ├── controller/  # 控制器层
│   │       ├── dto/         # 数据传输对象
│   │       ├── config/      # 配置类
│   │       ├── exception/   # 异常处理
│   │       └── util/        # 工具类
│   └── pom.xml
├── frontend/                # 前端
│   ├── index.html
│   ├── style.css
│   └── script.js
└── README.md
```

### 技术栈
- **后端**: Spring Boot 2.7.0, Spring Data JPA, SQLite
- **前端**: HTML5, CSS3, JavaScript (ES6+), Chart.js
- **构建工具**: Maven
- **数据库**: SQLite

## 许可证

本项目采用MIT许可证。
