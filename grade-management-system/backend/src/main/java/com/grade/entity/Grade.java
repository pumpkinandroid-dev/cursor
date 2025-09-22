package com.grade.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
public class Grade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id", nullable = false)
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号长度不能超过20个字符")
    private String studentId;
    
    @Column(name = "subject", nullable = false)
    @NotBlank(message = "科目不能为空")
    @Size(max = 50, message = "科目名称长度不能超过50个字符")
    private String subject;
    
    @Column(name = "score", nullable = false, precision = 5, scale = 2)
    @NotNull(message = "成绩不能为空")
    @DecimalMin(value = "0.0", message = "成绩不能小于0分")
    @DecimalMax(value = "100.0", message = "成绩不能大于100分")
    private BigDecimal score;
    
    @Column(name = "semester", nullable = false)
    @NotBlank(message = "学期不能为空")
    @Size(max = 20, message = "学期长度不能超过20个字符")
    private String semester;
    
    @Column(name = "exam_type")
    @Size(max = 20, message = "考试类型长度不能超过20个字符")
    private String examType;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private Student student;
    
    // 构造函数
    public Grade() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.examType = "期中考试";
    }
    
    public Grade(String studentId, String subject, BigDecimal score, String semester, String examType) {
        this();
        this.studentId = studentId;
        this.subject = subject;
        this.score = score;
        this.semester = semester;
        this.examType = examType;
    }
    
    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public BigDecimal getScore() {
        return score;
    }
    
    public void setScore(BigDecimal score) {
        this.score = score;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getExamType() {
        return examType;
    }
    
    public void setExamType(String examType) {
        this.examType = examType;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", subject='" + subject + '\'' +
                ", score=" + score +
                ", semester='" + semester + '\'' +
                ", examType='" + examType + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
