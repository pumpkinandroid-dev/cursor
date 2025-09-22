-- 创建学生表
CREATE TABLE IF NOT EXISTS students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    class_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建成绩表
CREATE TABLE IF NOT EXISTS grades (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id VARCHAR(20) NOT NULL,
    subject VARCHAR(50) NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    semester VARCHAR(20) NOT NULL,
    exam_type VARCHAR(20) DEFAULT '期中考试',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);

-- 插入示例数据
INSERT OR IGNORE INTO students (student_id, name, class_name) VALUES 
('2021001', '张三', '计算机科学与技术1班'),
('2021002', '李四', '计算机科学与技术1班'),
('2021003', '王五', '软件工程1班'),
('2021004', '赵六', '软件工程1班'),
('2021005', '钱七', '计算机科学与技术2班');

INSERT OR IGNORE INTO grades (student_id, subject, score, semester, exam_type) VALUES 
('2021001', '高等数学', 85.5, '2021-1', '期末考试'),
('2021001', '线性代数', 78.0, '2021-1', '期末考试'),
('2021001', '程序设计', 92.0, '2021-1', '期末考试'),
('2021002', '高等数学', 76.5, '2021-1', '期末考试'),
('2021002', '线性代数', 88.0, '2021-1', '期末考试'),
('2021002', '程序设计', 85.0, '2021-1', '期末考试'),
('2021003', '高等数学', 90.0, '2021-1', '期末考试'),
('2021003', '线性代数', 82.5, '2021-1', '期末考试'),
('2021003', '程序设计', 95.0, '2021-1', '期末考试'),
('2021004', '高等数学', 68.0, '2021-1', '期末考试'),
('2021004', '线性代数', 75.5, '2021-1', '期末考试'),
('2021004', '程序设计', 80.0, '2021-1', '期末考试'),
('2021005', '高等数学', 88.5, '2021-1', '期末考试'),
('2021005', '线性代数', 91.0, '2021-1', '期末考试'),
('2021005', '程序设计', 87.5, '2021-1', '期末考试');
