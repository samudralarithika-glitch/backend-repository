package com.example.leave.dto;

import com.example.leave.model.Leave;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveRequestDTO {

    public static class Request {

        @NotNull(message = "Leave type is required")
        private Leave.LeaveType leaveType;

        @NotNull(message = "Start date is required")
        private LocalDate startDate;

        @NotNull(message = "End date is required")
        private LocalDate endDate;

        @NotBlank(message = "Reason is required")
        private String reason;

        public Request() {}

        public Leave.LeaveType getLeaveType() { return leaveType; }
        public void setLeaveType(Leave.LeaveType leaveType) { this.leaveType = leaveType; }

        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    public static class StatusUpdate {

        @NotNull(message = "Status is required")
        private Leave.LeaveStatus status;

        private String adminRemarks;

        public StatusUpdate() {}

        public Leave.LeaveStatus getStatus() { return status; }
        public void setStatus(Leave.LeaveStatus status) { this.status = status; }

        public String getAdminRemarks() { return adminRemarks; }
        public void setAdminRemarks(String adminRemarks) { this.adminRemarks = adminRemarks; }
    }

    public static class Response {

        private Long id;
        private Long studentId;
        private String studentName;
        private String rollNumber;
        private Leave.LeaveType leaveType;
        private LocalDate startDate;
        private LocalDate endDate;
        private long durationDays;
        private String reason;
        private Leave.LeaveStatus status;
        private String adminRemarks;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Response() {}

        public static Response fromEntity(Leave leave) {
            Response r = new Response();
            r.id = leave.getId();
            r.studentId = leave.getStudent().getId();
            r.studentName = leave.getStudent().getFirstName() + " " + leave.getStudent().getLastName();
            r.rollNumber = leave.getStudent().getRollNumber();
            r.leaveType = leave.getLeaveType();
            r.startDate = leave.getStartDate();
            r.endDate = leave.getEndDate();
            r.durationDays = leave.getDurationDays();
            r.reason = leave.getReason();
            r.status = leave.getStatus();
            r.adminRemarks = leave.getAdminRemarks();
            r.createdAt = leave.getCreatedAt();
            r.updatedAt = leave.getUpdatedAt();
            return r;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }

        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }

        public String getRollNumber() { return rollNumber; }
        public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

        public Leave.LeaveType getLeaveType() { return leaveType; }
        public void setLeaveType(Leave.LeaveType leaveType) { this.leaveType = leaveType; }

        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

        public long getDurationDays() { return durationDays; }
        public void setDurationDays(long durationDays) { this.durationDays = durationDays; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }

        public Leave.LeaveStatus getStatus() { return status; }
        public void setStatus(Leave.LeaveStatus status) { this.status = status; }

        public String getAdminRemarks() { return adminRemarks; }
        public void setAdminRemarks(String adminRemarks) { this.adminRemarks = adminRemarks; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }

    public static class StudentRequest {

        @NotBlank(message = "First name is required")
        private String firstName;

        @NotBlank(message = "Last name is required")
        private String lastName;

        @NotBlank(message = "Roll number is required")
        private String rollNumber;

        @NotBlank(message = "Email is required")
        private String email;

        private String phone;

        @NotBlank(message = "Department is required")
        private String department;

        private String year;

        public StudentRequest() {}

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getRollNumber() { return rollNumber; }
        public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }

        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
    }

    public static class StudentResponse {

        private Long id;
        private String firstName;
        private String lastName;
        private String fullName;
        private String rollNumber;
        private String email;
        private String phone;
        private String department;
        private String year;
        private int totalLeaves;
        private int pendingLeaves;
        private int approvedLeaves;

        public StudentResponse() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getRollNumber() { return rollNumber; }
        public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }

        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }

        public int getTotalLeaves() { return totalLeaves; }
        public void setTotalLeaves(int totalLeaves) { this.totalLeaves = totalLeaves; }

        public int getPendingLeaves() { return pendingLeaves; }
        public void setPendingLeaves(int pendingLeaves) { this.pendingLeaves = pendingLeaves; }

        public int getApprovedLeaves() { return approvedLeaves; }
        public void setApprovedLeaves(int approvedLeaves) { this.approvedLeaves = approvedLeaves; }
    }
}