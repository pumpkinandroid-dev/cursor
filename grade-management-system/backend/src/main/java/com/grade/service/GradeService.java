package com.grade.service;

import com.grade.dao.GradeRepository;
import com.grade.entity.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GradeService {
    
    @Autowired
    private GradeRepository gradeRepository;
    
    /**
     * 获取所有成绩
     */
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }
    
    /**
     * 根据ID获取成绩
     */
    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }
    
    /**
     * 根据学号获取成绩
     */
    public List<Grade> getGradesByStudentId(String studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
    
    /**
     * 根据学号和学期获取成绩
     */
    public List<Grade> getGradesByStudentIdAndSemester(String studentId, String semester) {
        return gradeRepository.findByStudentIdAndSemester(studentId, semester);
    }
    
    /**
     * 添加成绩
     */
    public Grade addGrade(Grade grade) {
        // 检查是否已存在相同的成绩记录
        Optional<Grade> existingGrade = gradeRepository.findByStudentIdAndSubjectAndSemester(
                grade.getStudentId(), grade.getSubject(), grade.getSemester());
        
        if (existingGrade.isPresent()) {
            throw new RuntimeException("该学生在此学期的该科目成绩已存在");
        }
        
        return gradeRepository.save(grade);
    }
    
    /**
     * 更新成绩
     */
    public Grade updateGrade(Long id, Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("成绩不存在，ID: " + id));
        
        // 检查是否与其他记录冲突
        Optional<Grade> existingGrade = gradeRepository.findByStudentIdAndSubjectAndSemester(
                gradeDetails.getStudentId(), gradeDetails.getSubject(), gradeDetails.getSemester());
        
        if (existingGrade.isPresent() && !existingGrade.get().getId().equals(id)) {
            throw new RuntimeException("该学生在此学期的该科目成绩已存在");
        }
        
        grade.setStudentId(gradeDetails.getStudentId());
        grade.setSubject(gradeDetails.getSubject());
        grade.setScore(gradeDetails.getScore());
        grade.setSemester(gradeDetails.getSemester());
        grade.setExamType(gradeDetails.getExamType());
        
        return gradeRepository.save(grade);
    }
    
    /**
     * 删除成绩
     */
    public void deleteGrade(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("成绩不存在，ID: " + id));
        gradeRepository.delete(grade);
    }
    
    /**
     * 根据学号删除成绩
     */
    public void deleteGradesByStudentId(String studentId) {
        gradeRepository.deleteByStudentId(studentId);
    }
    
    /**
     * 根据学号、科目和学期删除成绩
     */
    public void deleteGradeByStudentIdAndSubjectAndSemester(String studentId, String subject, String semester) {
        gradeRepository.deleteByStudentIdAndSubjectAndSemester(studentId, subject, semester);
    }
    
    /**
     * 根据科目获取成绩
     */
    public List<Grade> getGradesBySubject(String subject) {
        return gradeRepository.findBySubject(subject);
    }
    
    /**
     * 根据学期获取成绩
     */
    public List<Grade> getGradesBySemester(String semester) {
        return gradeRepository.findBySemester(semester);
    }
    
    /**
     * 根据考试类型获取成绩
     */
    public List<Grade> getGradesByExamType(String examType) {
        return gradeRepository.findByExamType(examType);
    }
    
    /**
     * 根据班级获取成绩
     */
    public List<Grade> getGradesByClassName(String className) {
        return gradeRepository.findByClassName(className);
    }
    
    /**
     * 根据班级和学期获取成绩
     */
    public List<Grade> getGradesByClassNameAndSemester(String className, String semester) {
        return gradeRepository.findByClassNameAndSemester(className, semester);
    }
    
    /**
     * 搜索成绩
     */
    public List<Grade> searchGrades(String keyword) {
        return gradeRepository.searchGrades(keyword);
    }
    
    /**
     * 获取所有科目
     */
    public List<String> getAllSubjects() {
        return gradeRepository.findAllSubjects();
    }
    
    /**
     * 获取所有学期
     */
    public List<String> getAllSemesters() {
        return gradeRepository.findAllSemesters();
    }
    
    /**
     * 获取所有考试类型
     */
    public List<String> getAllExamTypes() {
        return gradeRepository.findAllExamTypes();
    }
    
    /**
     * 计算学生平均分
     */
    public BigDecimal calculateStudentAverageScore(String studentId) {
        Optional<BigDecimal> average = gradeRepository.calculateAverageScoreByStudentId(studentId);
        return average.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 计算学生指定学期的平均分
     */
    public BigDecimal calculateStudentAverageScoreBySemester(String studentId, String semester) {
        Optional<BigDecimal> average = gradeRepository.calculateAverageScoreByStudentIdAndSemester(studentId, semester);
        return average.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 计算班级平均分
     */
    public BigDecimal calculateClassAverageScore(String className) {
        Optional<BigDecimal> average = gradeRepository.calculateAverageScoreByClassName(className);
        return average.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 计算班级指定学期的平均分
     */
    public BigDecimal calculateClassAverageScoreBySemester(String className, String semester) {
        Optional<BigDecimal> average = gradeRepository.calculateAverageScoreByClassNameAndSemester(className, semester);
        return average.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 计算科目平均分
     */
    public BigDecimal calculateSubjectAverageScore(String subject) {
        Optional<BigDecimal> average = gradeRepository.calculateAverageScoreBySubject(subject);
        return average.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 计算科目指定学期的平均分
     */
    public BigDecimal calculateSubjectAverageScoreBySemester(String subject, String semester) {
        Optional<BigDecimal> average = gradeRepository.calculateAverageScoreBySubjectAndSemester(subject, semester);
        return average.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 获取成绩统计信息
     */
    public Object[] getGradeStatistics() {
        return gradeRepository.getGradeStatistics();
    }
    
    /**
     * 获取成绩总数
     */
    public long getTotalGradeCount() {
        return gradeRepository.count();
    }
}
