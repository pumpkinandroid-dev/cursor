package com.grade.service;

import com.grade.dao.StudentRepository;
import com.grade.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * 获取所有学生
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    /**
     * 根据ID获取学生
     */
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    /**
     * 根据学号获取学生
     */
    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }
    
    /**
     * 添加学生
     */
    public Student addStudent(Student student) {
        // 检查学号是否已存在
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new RuntimeException("学号已存在: " + student.getStudentId());
        }
        return studentRepository.save(student);
    }
    
    /**
     * 更新学生信息
     */
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在，ID: " + id));
        
        // 检查学号是否被其他学生使用
        if (!student.getStudentId().equals(studentDetails.getStudentId()) && 
            studentRepository.existsByStudentId(studentDetails.getStudentId())) {
            throw new RuntimeException("学号已存在: " + studentDetails.getStudentId());
        }
        
        student.setStudentId(studentDetails.getStudentId());
        student.setName(studentDetails.getName());
        student.setClassName(studentDetails.getClassName());
        
        return studentRepository.save(student);
    }
    
    /**
     * 删除学生
     */
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在，ID: " + id));
        studentRepository.delete(student);
    }
    
    /**
     * 根据学号删除学生
     */
    public void deleteStudentByStudentId(String studentId) {
        if (!studentRepository.existsByStudentId(studentId)) {
            throw new RuntimeException("学生不存在，学号: " + studentId);
        }
        studentRepository.deleteByStudentId(studentId);
    }
    
    /**
     * 根据姓名搜索学生
     */
    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContaining(name);
    }
    
    /**
     * 根据班级获取学生
     */
    public List<Student> getStudentsByClassName(String className) {
        return studentRepository.findByClassName(className);
    }
    
    /**
     * 根据姓名和班级搜索学生
     */
    public List<Student> searchStudentsByNameAndClassName(String name, String className) {
        return studentRepository.findByNameContainingAndClassName(name, className);
    }
    
    /**
     * 搜索学生（综合搜索）
     */
    public List<Student> searchStudents(String keyword) {
        return studentRepository.searchStudents(keyword);
    }
    
    /**
     * 获取所有班级
     */
    public List<String> getAllClassNames() {
        return studentRepository.findAllClassNames();
    }
    
    /**
     * 根据班级统计学生数量
     */
    public long countStudentsByClassName(String className) {
        return studentRepository.countByClassName(className);
    }
    
    /**
     * 检查学号是否存在
     */
    public boolean existsByStudentId(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }
    
    /**
     * 获取学生总数
     */
    public long getTotalStudentCount() {
        return studentRepository.count();
    }
}
