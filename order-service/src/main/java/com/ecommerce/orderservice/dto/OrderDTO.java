package com.ecommerce.orderservice.dto;

public class OrderDTO {
    
    private Long id;
    private Long studentId;
    private Long courseId;
    private Double totalAmount;

    public OrderDTO() {}

    public OrderDTO(Long id, Long studentId, Long courseId, Double totalAmount) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
