package com.grade.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StudentDTO {
    
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号长度不能超过20个字符")
    private String studentId;
    
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;
    
    @NotBlank(message = "班级不能为空")
    @Size(max = 50, message = "班级名称长度不能超过50个字符")
    private String className;
    
    // 构造函数
    public StudentDTO() {}
    
    public StudentDTO(String studentId, String name, String className) {
        this.studentId = studentId;
        this.name = name;
        this.className = className;
    }
    
    // Getter 和 Setter 方法
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    @Override
    public String toString() {
        return "StudentDTO{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
