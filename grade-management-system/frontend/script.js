// API基础URL
const API_BASE_URL = 'http://localhost:8080/api';

// 全局变量
let students = [];
let grades = [];
let currentTab = 'students';

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

// 初始化应用
async function initializeApp() {
    showLoading();
    try {
        await loadStudents();
        await loadGrades();
        await loadStatistics();
        setupEventListeners();
    } catch (error) {
        showMessage('初始化失败: ' + error.message, 'error');
    } finally {
        hideLoading();
    }
}

// 设置事件监听器
function setupEventListeners() {
    // 学生表单
    document.getElementById('addStudentForm').addEventListener('submit', handleAddStudent);
    document.getElementById('editStudentForm').addEventListener('submit', handleEditStudent);
    
    // 成绩表单
    document.getElementById('addGradeForm').addEventListener('submit', handleAddGrade);
    document.getElementById('editGradeForm').addEventListener('submit', handleEditGrade);
    
    // 模态框点击外部关闭
    window.addEventListener('click', function(event) {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = 'none';
        }
    });
}

// 标签页切换
function switchTab(tabName) {
    // 隐藏所有标签内容
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    
    // 移除所有标签按钮的active类
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // 显示选中的标签内容
    document.getElementById(tabName + '-tab').classList.add('active');
    
    // 激活选中的标签按钮
    event.target.classList.add('active');
    
    currentTab = tabName;
    
    // 根据标签页加载相应数据
    if (tabName === 'statistics') {
        loadStatistics();
    }
}

// ==================== 学生管理 ====================

// 加载学生数据
async function loadStudents() {
    try {
        const response = await fetch(`${API_BASE_URL}/students`);
        if (!response.ok) throw new Error('加载学生数据失败');
        
        students = await response.json();
        renderStudentsTable();
        updateStudentSelects();
    } catch (error) {
        showMessage('加载学生数据失败: ' + error.message, 'error');
    }
}

// 渲染学生表格
function renderStudentsTable() {
    const tbody = document.getElementById('studentsTableBody');
    tbody.innerHTML = '';
    
    students.forEach(student => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${student.studentId}</td>
            <td>${student.name}</td>
            <td>${student.className}</td>
            <td>${formatDate(student.createdAt)}</td>
            <td>
                <button class="btn btn-warning" onclick="editStudent(${student.id})">
                    <i class="fas fa-edit"></i> 编辑
                </button>
                <button class="btn btn-danger" onclick="deleteStudent(${student.id})">
                    <i class="fas fa-trash"></i> 删除
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// 搜索学生
async function searchStudents() {
    const keyword = document.getElementById('studentSearch').value.trim();
    
    if (!keyword) {
        await loadStudents();
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/students/search?keyword=${encodeURIComponent(keyword)}`);
        if (!response.ok) throw new Error('搜索失败');
        
        students = await response.json();
        renderStudentsTable();
    } catch (error) {
        showMessage('搜索失败: ' + error.message, 'error');
    }
}

// 清除学生搜索
function clearStudentSearch() {
    document.getElementById('studentSearch').value = '';
    loadStudents();
}

// 显示添加学生模态框
function showAddStudentModal() {
    document.getElementById('addStudentModal').style.display = 'block';
    document.getElementById('addStudentForm').reset();
}

// 显示编辑学生模态框
function editStudent(id) {
    const student = students.find(s => s.id === id);
    if (!student) return;
    
    document.getElementById('editStudentId').value = student.id;
    document.getElementById('editStudentIdField').value = student.studentId;
    document.getElementById('editStudentName').value = student.name;
    document.getElementById('editClassName').value = student.className;
    
    document.getElementById('editStudentModal').style.display = 'block';
}

// 处理添加学生
async function handleAddStudent(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const studentData = {
        studentId: formData.get('studentId'),
        name: formData.get('name'),
        className: formData.get('className')
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/students`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(studentData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || '添加学生失败');
        }
        
        const newStudent = await response.json();
        students.push(newStudent);
        renderStudentsTable();
        updateStudentSelects();
        closeModal('addStudentModal');
        showMessage('学生添加成功', 'success');
    } catch (error) {
        showMessage('添加学生失败: ' + error.message, 'error');
    }
}

// 处理编辑学生
async function handleEditStudent(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const id = formData.get('id');
    const studentData = {
        studentId: formData.get('studentId'),
        name: formData.get('name'),
        className: formData.get('className')
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/students/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(studentData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || '更新学生失败');
        }
        
        const updatedStudent = await response.json();
        const index = students.findIndex(s => s.id === id);
        if (index !== -1) {
            students[index] = updatedStudent;
        }
        
        renderStudentsTable();
        updateStudentSelects();
        closeModal('editStudentModal');
        showMessage('学生信息更新成功', 'success');
    } catch (error) {
        showMessage('更新学生失败: ' + error.message, 'error');
    }
}

// 删除学生
async function deleteStudent(id) {
    if (!confirm('确定要删除这个学生吗？这将同时删除该学生的所有成绩记录。')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/students/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || '删除学生失败');
        }
        
        students = students.filter(s => s.id !== id);
        grades = grades.filter(g => g.studentId !== students.find(s => s.id === id)?.studentId);
        
        renderStudentsTable();
        renderGradesTable();
        updateStudentSelects();
        showMessage('学生删除成功', 'success');
    } catch (error) {
        showMessage('删除学生失败: ' + error.message, 'error');
    }
}

// ==================== 成绩管理 ====================

// 加载成绩数据
async function loadGrades() {
    try {
        const response = await fetch(`${API_BASE_URL}/grades`);
        if (!response.ok) throw new Error('加载成绩数据失败');
        
        grades = await response.json();
        renderGradesTable();
        updateFilters();
    } catch (error) {
        showMessage('加载成绩数据失败: ' + error.message, 'error');
    }
}

// 渲染成绩表格
function renderGradesTable() {
    const tbody = document.getElementById('gradesTableBody');
    tbody.innerHTML = '';
    
    grades.forEach(grade => {
        const student = students.find(s => s.studentId === grade.studentId);
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${grade.studentId}</td>
            <td>${student ? student.name : '未知'}</td>
            <td>${grade.subject}</td>
            <td>${grade.score}</td>
            <td>${grade.semester}</td>
            <td>${grade.examType}</td>
            <td>
                <button class="btn btn-warning" onclick="editGrade(${grade.id})">
                    <i class="fas fa-edit"></i> 编辑
                </button>
                <button class="btn btn-danger" onclick="deleteGrade(${grade.id})">
                    <i class="fas fa-trash"></i> 删除
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// 搜索成绩
async function searchGrades() {
    const keyword = document.getElementById('gradeSearch').value.trim();
    
    if (!keyword) {
        await loadGrades();
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/grades/search?keyword=${encodeURIComponent(keyword)}`);
        if (!response.ok) throw new Error('搜索失败');
        
        grades = await response.json();
        renderGradesTable();
    } catch (error) {
        showMessage('搜索失败: ' + error.message, 'error');
    }
}

// 过滤成绩
function filterGrades() {
    const studentFilter = document.getElementById('gradeStudentFilter').value;
    const subjectFilter = document.getElementById('gradeSubjectFilter').value;
    const semesterFilter = document.getElementById('gradeSemesterFilter').value;
    
    let filteredGrades = grades;
    
    if (studentFilter) {
        filteredGrades = filteredGrades.filter(g => g.studentId === studentFilter);
    }
    
    if (subjectFilter) {
        filteredGrades = filteredGrades.filter(g => g.subject === subjectFilter);
    }
    
    if (semesterFilter) {
        filteredGrades = filteredGrades.filter(g => g.semester === semesterFilter);
    }
    
    // 临时保存原始数据
    const originalGrades = grades;
    grades = filteredGrades;
    renderGradesTable();
    grades = originalGrades;
}

// 显示添加成绩模态框
function showAddGradeModal() {
    document.getElementById('addGradeModal').style.display = 'block';
    document.getElementById('addGradeForm').reset();
    updateStudentSelects();
}

// 显示编辑成绩模态框
function editGrade(id) {
    const grade = grades.find(g => g.id === id);
    if (!grade) return;
    
    document.getElementById('editGradeId').value = grade.id;
    document.getElementById('editGradeStudentId').value = grade.studentId;
    document.getElementById('editGradeSubject').value = grade.subject;
    document.getElementById('editGradeScore').value = grade.score;
    document.getElementById('editGradeSemester').value = grade.semester;
    document.getElementById('editGradeExamType').value = grade.examType;
    
    document.getElementById('editGradeModal').style.display = 'block';
    updateStudentSelects();
}

// 处理添加成绩
async function handleAddGrade(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const gradeData = {
        studentId: formData.get('studentId'),
        subject: formData.get('subject'),
        score: parseFloat(formData.get('score')),
        semester: formData.get('semester'),
        examType: formData.get('examType')
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/grades`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(gradeData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || '添加成绩失败');
        }
        
        const newGrade = await response.json();
        grades.push(newGrade);
        renderGradesTable();
        updateFilters();
        closeModal('addGradeModal');
        showMessage('成绩添加成功', 'success');
    } catch (error) {
        showMessage('添加成绩失败: ' + error.message, 'error');
    }
}

// 处理编辑成绩
async function handleEditGrade(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const id = formData.get('id');
    const gradeData = {
        studentId: formData.get('studentId'),
        subject: formData.get('subject'),
        score: parseFloat(formData.get('score')),
        semester: formData.get('semester'),
        examType: formData.get('examType')
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/grades/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(gradeData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || '更新成绩失败');
        }
        
        const updatedGrade = await response.json();
        const index = grades.findIndex(g => g.id === id);
        if (index !== -1) {
            grades[index] = updatedGrade;
        }
        
        renderGradesTable();
        updateFilters();
        closeModal('editGradeModal');
        showMessage('成绩更新成功', 'success');
    } catch (error) {
        showMessage('更新成绩失败: ' + error.message, 'error');
    }
}

// 删除成绩
async function deleteGrade(id) {
    if (!confirm('确定要删除这个成绩记录吗？')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/grades/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || '删除成绩失败');
        }
        
        grades = grades.filter(g => g.id !== id);
        renderGradesTable();
        showMessage('成绩删除成功', 'success');
    } catch (error) {
        showMessage('删除成绩失败: ' + error.message, 'error');
    }
}

// ==================== 统计分析 ====================

// 加载统计数据
async function loadStatistics() {
    try {
        // 加载基本统计
        const [studentsResponse, gradesResponse, statsResponse] = await Promise.all([
            fetch(`${API_BASE_URL}/students/count`),
            fetch(`${API_BASE_URL}/grades/count`),
            fetch(`${API_BASE_URL}/grades/statistics`)
        ]);
        
        if (!studentsResponse.ok || !gradesResponse.ok || !statsResponse.ok) {
            throw new Error('加载统计数据失败');
        }
        
        const studentsCount = await studentsResponse.json();
        const gradesCount = await gradesResponse.json();
        const stats = await statsResponse.json();
        
        // 更新统计卡片
        document.getElementById('totalStudents').textContent = studentsCount.totalCount;
        document.getElementById('totalGrades').textContent = gradesCount.totalCount;
        document.getElementById('averageScore').textContent = stats.averageScore ? stats.averageScore.toFixed(2) : '0';
        document.getElementById('maxScore').textContent = stats.maxScore || '0';
        
        // 加载图表数据
        await loadCharts();
    } catch (error) {
        showMessage('加载统计数据失败: ' + error.message, 'error');
    }
}

// 加载图表
async function loadCharts() {
    try {
        // 班级成绩分布
        const classStats = await getClassStatistics();
        renderClassChart(classStats);
        
        // 科目成绩分布
        const subjectStats = await getSubjectStatistics();
        renderSubjectChart(subjectStats);
    } catch (error) {
        console.error('加载图表失败:', error);
    }
}

// 获取班级统计
async function getClassStatistics() {
    const classNames = [...new Set(students.map(s => s.className))];
    const stats = [];
    
    for (const className of classNames) {
        try {
            const response = await fetch(`${API_BASE_URL}/grades/class/${encodeURIComponent(className)}/average`);
            if (response.ok) {
                const data = await response.json();
                stats.push({
                    className: className,
                    averageScore: data.averageScore || 0
                });
            }
        } catch (error) {
            console.error(`获取班级 ${className} 统计失败:`, error);
        }
    }
    
    return stats;
}

// 获取科目统计
async function getSubjectStatistics() {
    const subjects = [...new Set(grades.map(g => g.subject))];
    const stats = [];
    
    for (const subject of subjects) {
        try {
            const response = await fetch(`${API_BASE_URL}/grades/subject/${encodeURIComponent(subject)}/average`);
            if (response.ok) {
                const data = await response.json();
                stats.push({
                    subject: subject,
                    averageScore: data.averageScore || 0
                });
            }
        } catch (error) {
            console.error(`获取科目 ${subject} 统计失败:`, error);
        }
    }
    
    return stats;
}

// 渲染班级图表
function renderClassChart(data) {
    const ctx = document.getElementById('classChart').getContext('2d');
    
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: data.map(d => d.className),
            datasets: [{
                label: '平均分',
                data: data.map(d => d.averageScore),
                backgroundColor: 'rgba(102, 126, 234, 0.8)',
                borderColor: 'rgba(102, 126, 234, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100
                }
            }
        }
    });
}

// 渲染科目图表
function renderSubjectChart(data) {
    const ctx = document.getElementById('subjectChart').getContext('2d');
    
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: data.map(d => d.subject),
            datasets: [{
                data: data.map(d => d.averageScore),
                backgroundColor: [
                    'rgba(102, 126, 234, 0.8)',
                    'rgba(72, 187, 120, 0.8)',
                    'rgba(237, 137, 54, 0.8)',
                    'rgba(245, 101, 101, 0.8)',
                    'rgba(159, 122, 234, 0.8)',
                    'rgba(236, 201, 75, 0.8)'
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

// ==================== 工具函数 ====================

// 更新学生选择框
function updateStudentSelects() {
    const selects = [
        'gradeStudentId',
        'editGradeStudentId'
    ];
    
    selects.forEach(selectId => {
        const select = document.getElementById(selectId);
        if (select) {
            const currentValue = select.value;
            select.innerHTML = '<option value="">请选择学生</option>';
            
            students.forEach(student => {
                const option = document.createElement('option');
                option.value = student.studentId;
                option.textContent = `${student.studentId} - ${student.name}`;
                select.appendChild(option);
            });
            
            select.value = currentValue;
        }
    });
}

// 更新过滤器
async function updateFilters() {
    try {
        // 更新学生过滤器
        const studentFilter = document.getElementById('gradeStudentFilter');
        const currentStudentValue = studentFilter.value;
        studentFilter.innerHTML = '<option value="">所有学生</option>';
        
        students.forEach(student => {
            const option = document.createElement('option');
            option.value = student.studentId;
            option.textContent = `${student.studentId} - ${student.name}`;
            studentFilter.appendChild(option);
        });
        studentFilter.value = currentStudentValue;
        
        // 更新科目过滤器
        const subjectFilter = document.getElementById('gradeSubjectFilter');
        const currentSubjectValue = subjectFilter.value;
        subjectFilter.innerHTML = '<option value="">所有科目</option>';
        
        const subjects = [...new Set(grades.map(g => g.subject))];
        subjects.forEach(subject => {
            const option = document.createElement('option');
            option.value = subject;
            option.textContent = subject;
            subjectFilter.appendChild(option);
        });
        subjectFilter.value = currentSubjectValue;
        
        // 更新学期过滤器
        const semesterFilter = document.getElementById('gradeSemesterFilter');
        const currentSemesterValue = semesterFilter.value;
        semesterFilter.innerHTML = '<option value="">所有学期</option>';
        
        const semesters = [...new Set(grades.map(g => g.semester))];
        semesters.forEach(semester => {
            const option = document.createElement('option');
            option.value = semester;
            option.textContent = semester;
            semesterFilter.appendChild(option);
        });
        semesterFilter.value = currentSemesterValue;
    } catch (error) {
        console.error('更新过滤器失败:', error);
    }
}

// 关闭模态框
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// 显示加载动画
function showLoading() {
    document.getElementById('loading').style.display = 'flex';
}

// 隐藏加载动画
function hideLoading() {
    document.getElementById('loading').style.display = 'none';
}

// 显示消息
function showMessage(message, type = 'info') {
    const messageEl = document.getElementById('message');
    messageEl.textContent = message;
    messageEl.className = `message ${type}`;
    messageEl.classList.add('show');
    
    setTimeout(() => {
        messageEl.classList.remove('show');
    }, 3000);
}

// 格式化日期
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { hour12: false });
}
