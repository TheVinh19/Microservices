package com.ecommerce.orderservice.dto;

import jakarta.validation.constraints.NotNull;

public class OrderRequestDTO {
    
    @NotNull(message = "Student ID cannot be null")
    private Long studentId;
    
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
