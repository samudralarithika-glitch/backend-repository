package com.example.leave.service;

import com.example.leave.dto.LeaveRequestDTO;
import com.example.leave.model.Student;
import java.util.List;

public interface StudentService {
    LeaveRequestDTO.StudentResponse createStudent(LeaveRequestDTO.StudentRequest request, Long userId);
    LeaveRequestDTO.StudentResponse getStudentById(Long id);
    LeaveRequestDTO.StudentResponse getStudentByRollNumber(String rollNumber);
    LeaveRequestDTO.StudentResponse getStudentByUserId(Long userId);
    List<LeaveRequestDTO.StudentResponse> getAllStudents();
    List<LeaveRequestDTO.StudentResponse> getStudentsByDepartment(String department);
    List<LeaveRequestDTO.StudentResponse> searchStudentsByName(String name);
    LeaveRequestDTO.StudentResponse updateStudent(Long id, LeaveRequestDTO.StudentRequest request);
    void deleteStudent(Long id);
    Student getStudentEntityById(Long id);
}