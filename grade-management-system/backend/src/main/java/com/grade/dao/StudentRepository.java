package com.grade.dao;

import com.grade.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * 根据学号查找学生
     */
    Optional<Student> findByStudentId(String studentId);
    
    /**
     * 根据姓名查找学生
     */
    List<Student> findByNameContaining(String name);
    
    /**
     * 根据班级查找学生
     */
    List<Student> findByClassName(String className);
    
    /**
     * 根据姓名和班级查找学生
     */
    List<Student> findByNameContainingAndClassName(String name, String className);
    
    /**
     * 检查学号是否存在
     */
    boolean existsByStudentId(String studentId);
    
    /**
     * 根据学号删除学生
     */
    void deleteByStudentId(String studentId);
    
    /**
     * 获取所有班级列表
     */
    @Query("SELECT DISTINCT s.className FROM Student s ORDER BY s.className")
    List<String> findAllClassNames();
    
    /**
     * 根据班级统计学生数量
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.className = :className")
    long countByClassName(@Param("className") String className);
    
    /**
     * 搜索学生（按学号、姓名、班级）
     */
    @Query("SELECT s FROM Student s WHERE " +
           "s.studentId LIKE %:keyword% OR " +
           "s.name LIKE %:keyword% OR " +
           "s.className LIKE %:keyword%")
    List<Student> searchStudents(@Param("keyword") String keyword);
}
