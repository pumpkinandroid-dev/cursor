package com.grade.controller;

import com.grade.entity.Grade;
import com.grade.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*")
public class GradeController {
    
    @Autowired
    private GradeService gradeService;
    
    /**
     * 获取所有成绩
     */
    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        try {
            List<Grade> grades = gradeService.getAllGrades();
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据ID获取成绩
     */
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        try {
            Optional<Grade> grade = gradeService.getGradeById(id);
            return grade.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据学号获取成绩
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudentId(@PathVariable String studentId) {
        try {
            List<Grade> grades = gradeService.getGradesByStudentId(studentId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据学号和学期获取成绩
     */
    @GetMapping("/student/{studentId}/semester/{semester}")
    public ResponseEntity<List<Grade>> getGradesByStudentIdAndSemester(
            @PathVariable String studentId, @PathVariable String semester) {
        try {
            List<Grade> grades = gradeService.getGradesByStudentIdAndSemester(studentId, semester);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 添加成绩
     */
    @PostMapping
    public ResponseEntity<?> addGrade(@Valid @RequestBody Grade grade) {
        try {
            Grade savedGrade = gradeService.addGrade(grade);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "添加成绩失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 更新成绩
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrade(@PathVariable Long id, @Valid @RequestBody Grade gradeDetails) {
        try {
            Grade updatedGrade = gradeService.updateGrade(id, gradeDetails);
            return ResponseEntity.ok(updatedGrade);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "更新成绩失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 删除成绩
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrade(@PathVariable Long id) {
        try {
            gradeService.deleteGrade(id);
            Map<String, String> message = new HashMap<>();
            message.put("message", "成绩删除成功");
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "删除成绩失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 根据学号删除成绩
     */
    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<?> deleteGradesByStudentId(@PathVariable String studentId) {
        try {
            gradeService.deleteGradesByStudentId(studentId);
            Map<String, String> message = new HashMap<>();
            message.put("message", "学生成绩删除成功");
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "删除学生成绩失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 搜索成绩
     */
    @GetMapping("/search")
    public ResponseEntity<List<Grade>> searchGrades(@RequestParam String keyword) {
        try {
            List<Grade> grades = gradeService.searchGrades(keyword);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据科目获取成绩
     */
    @GetMapping("/subject/{subject}")
    public ResponseEntity<List<Grade>> getGradesBySubject(@PathVariable String subject) {
        try {
            List<Grade> grades = gradeService.getGradesBySubject(subject);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据学期获取成绩
     */
    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<Grade>> getGradesBySemester(@PathVariable String semester) {
        try {
            List<Grade> grades = gradeService.getGradesBySemester(semester);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据班级获取成绩
     */
    @GetMapping("/class/{className}")
    public ResponseEntity<List<Grade>> getGradesByClassName(@PathVariable String className) {
        try {
            List<Grade> grades = gradeService.getGradesByClassName(className);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据班级和学期获取成绩
     */
    @GetMapping("/class/{className}/semester/{semester}")
    public ResponseEntity<List<Grade>> getGradesByClassNameAndSemester(
            @PathVariable String className, @PathVariable String semester) {
        try {
            List<Grade> grades = gradeService.getGradesByClassNameAndSemester(className, semester);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取所有科目
     */
    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getAllSubjects() {
        try {
            List<String> subjects = gradeService.getAllSubjects();
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取所有学期
     */
    @GetMapping("/semesters")
    public ResponseEntity<List<String>> getAllSemesters() {
        try {
            List<String> semesters = gradeService.getAllSemesters();
            return ResponseEntity.ok(semesters);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取所有考试类型
     */
    @GetMapping("/exam-types")
    public ResponseEntity<List<String>> getAllExamTypes() {
        try {
            List<String> examTypes = gradeService.getAllExamTypes();
            return ResponseEntity.ok(examTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 计算学生平均分
     */
    @GetMapping("/student/{studentId}/average")
    public ResponseEntity<Map<String, Object>> calculateStudentAverageScore(@PathVariable String studentId) {
        try {
            BigDecimal average = gradeService.calculateStudentAverageScore(studentId);
            Map<String, Object> result = new HashMap<>();
            result.put("studentId", studentId);
            result.put("averageScore", average);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 计算学生指定学期的平均分
     */
    @GetMapping("/student/{studentId}/semester/{semester}/average")
    public ResponseEntity<Map<String, Object>> calculateStudentAverageScoreBySemester(
            @PathVariable String studentId, @PathVariable String semester) {
        try {
            BigDecimal average = gradeService.calculateStudentAverageScoreBySemester(studentId, semester);
            Map<String, Object> result = new HashMap<>();
            result.put("studentId", studentId);
            result.put("semester", semester);
            result.put("averageScore", average);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 计算班级平均分
     */
    @GetMapping("/class/{className}/average")
    public ResponseEntity<Map<String, Object>> calculateClassAverageScore(@PathVariable String className) {
        try {
            BigDecimal average = gradeService.calculateClassAverageScore(className);
            Map<String, Object> result = new HashMap<>();
            result.put("className", className);
            result.put("averageScore", average);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 计算科目平均分
     */
    @GetMapping("/subject/{subject}/average")
    public ResponseEntity<Map<String, Object>> calculateSubjectAverageScore(@PathVariable String subject) {
        try {
            BigDecimal average = gradeService.calculateSubjectAverageScore(subject);
            Map<String, Object> result = new HashMap<>();
            result.put("subject", subject);
            result.put("averageScore", average);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取成绩统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getGradeStatistics() {
        try {
            Object[] stats = gradeService.getGradeStatistics();
            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", stats[0]);
            result.put("averageScore", stats[1]);
            result.put("maxScore", stats[2]);
            result.put("minScore", stats[3]);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 获取成绩总数
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getTotalGradeCount() {
        try {
            long count = gradeService.getTotalGradeCount();
            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", count);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
