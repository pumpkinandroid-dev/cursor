package com.grade.dao;

import com.grade.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    /**
     * 根据学号查找成绩
     */
    List<Grade> findByStudentId(String studentId);
    
    /**
     * 根据学号和学期查找成绩
     */
    List<Grade> findByStudentIdAndSemester(String studentId, String semester);
    
    /**
     * 根据学号、科目和学期查找成绩
     */
    Optional<Grade> findByStudentIdAndSubjectAndSemester(String studentId, String subject, String semester);
    
    /**
     * 根据科目查找成绩
     */
    List<Grade> findBySubject(String subject);
    
    /**
     * 根据学期查找成绩
     */
    List<Grade> findBySemester(String semester);
    
    /**
     * 根据考试类型查找成绩
     */
    List<Grade> findByExamType(String examType);
    
    /**
     * 根据班级查找成绩
     */
    @Query("SELECT g FROM Grade g JOIN g.student s WHERE s.className = :className")
    List<Grade> findByClassName(@Param("className") String className);
    
    /**
     * 根据班级和学期查找成绩
     */
    @Query("SELECT g FROM Grade g JOIN g.student s WHERE s.className = :className AND g.semester = :semester")
    List<Grade> findByClassNameAndSemester(@Param("className") String className, @Param("semester") String semester);
    
    /**
     * 根据学号删除成绩
     */
    void deleteByStudentId(String studentId);
    
    /**
     * 根据学号、科目和学期删除成绩
     */
    void deleteByStudentIdAndSubjectAndSemester(String studentId, String subject, String semester);
    
    /**
     * 获取所有科目列表
     */
    @Query("SELECT DISTINCT g.subject FROM Grade g ORDER BY g.subject")
    List<String> findAllSubjects();
    
    /**
     * 获取所有学期列表
     */
    @Query("SELECT DISTINCT g.semester FROM Grade g ORDER BY g.semester")
    List<String> findAllSemesters();
    
    /**
     * 获取所有考试类型列表
     */
    @Query("SELECT DISTINCT g.examType FROM Grade g ORDER BY g.examType")
    List<String> findAllExamTypes();
    
    /**
     * 计算学生平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.studentId = :studentId")
    Optional<BigDecimal> calculateAverageScoreByStudentId(@Param("studentId") String studentId);
    
    /**
     * 计算学生指定学期的平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.studentId = :studentId AND g.semester = :semester")
    Optional<BigDecimal> calculateAverageScoreByStudentIdAndSemester(@Param("studentId") String studentId, @Param("semester") String semester);
    
    /**
     * 计算班级平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g JOIN g.student s WHERE s.className = :className")
    Optional<BigDecimal> calculateAverageScoreByClassName(@Param("className") String className);
    
    /**
     * 计算班级指定学期的平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g JOIN g.student s WHERE s.className = :className AND g.semester = :semester")
    Optional<BigDecimal> calculateAverageScoreByClassNameAndSemester(@Param("className") String className, @Param("semester") String semester);
    
    /**
     * 计算科目平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.subject = :subject")
    Optional<BigDecimal> calculateAverageScoreBySubject(@Param("subject") String subject);
    
    /**
     * 计算科目指定学期的平均分
     */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.subject = :subject AND g.semester = :semester")
    Optional<BigDecimal> calculateAverageScoreBySubjectAndSemester(@Param("subject") String subject, @Param("semester") String semester);
    
    /**
     * 搜索成绩（按学号、科目、学期）
     */
    @Query("SELECT g FROM Grade g WHERE " +
           "g.studentId LIKE %:keyword% OR " +
           "g.subject LIKE %:keyword% OR " +
           "g.semester LIKE %:keyword%")
    List<Grade> searchGrades(@Param("keyword") String keyword);
    
    /**
     * 获取成绩统计信息
     */
    @Query("SELECT " +
           "COUNT(g) as totalCount, " +
           "AVG(g.score) as averageScore, " +
           "MAX(g.score) as maxScore, " +
           "MIN(g.score) as minScore " +
           "FROM Grade g")
    Object[] getGradeStatistics();
}
