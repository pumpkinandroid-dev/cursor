package com.grade.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class GradeDTO {
    
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号长度不能超过20个字符")
    private String studentId;
    
    @NotBlank(message = "科目不能为空")
    @Size(max = 50, message = "科目名称长度不能超过50个字符")
    private String subject;
    
    @NotNull(message = "成绩不能为空")
    @DecimalMin(value = "0.0", message = "成绩不能小于0分")
    @DecimalMax(value = "100.0", message = "成绩不能大于100分")
    private BigDecimal score;
    
    @NotBlank(message = "学期不能为空")
    @Size(max = 20, message = "学期长度不能超过20个字符")
    private String semester;
    
    @Size(max = 20, message = "考试类型长度不能超过20个字符")
    private String examType;
    
    // 构造函数
    public GradeDTO() {
        this.examType = "期中考试";
    }
    
    public GradeDTO(String studentId, String subject, BigDecimal score, String semester, String examType) {
        this();
        this.studentId = studentId;
        this.subject = subject;
        this.score = score;
        this.semester = semester;
        this.examType = examType;
    }
    
    // Getter 和 Setter 方法
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
    
    @Override
    public String toString() {
        return "GradeDTO{" +
                "studentId='" + studentId + '\'' +
                ", subject='" + subject + '\'' +
                ", score=" + score +
                ", semester='" + semester + '\'' +
                ", examType='" + examType + '\'' +
                '}';
    }
}
