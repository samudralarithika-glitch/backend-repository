package com.example.leave.controller;

import com.example.leave.dto.LeaveRequestDTO;
import com.example.leave.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<LeaveRequestDTO.StudentResponse> createStudent(
            @Valid @RequestBody LeaveRequestDTO.StudentRequest request) {
        LeaveRequestDTO.StudentResponse response = studentService.createStudent(request, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequestDTO.StudentResponse>> getAllStudents(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String name) {
        if (name != null && !name.trim().isEmpty()) {
            return ResponseEntity.ok(studentService.searchStudentsByName(name));
        }
        if (department != null && !department.trim().isEmpty()) {
            return ResponseEntity.ok(studentService.getStudentsByDepartment(department));
        }
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestDTO.StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/roll/{rollNumber}")
    public ResponseEntity<LeaveRequestDTO.StudentResponse> getStudentByRollNumber(
            @PathVariable String rollNumber) {
        return ResponseEntity.ok(studentService.getStudentByRollNumber(rollNumber));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequestDTO.StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestDTO.StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        Map<String, String> response = new HashMap<String, String>();
        response.put("message", "Student deleted successfully");
        return ResponseEntity.ok(response);
    }
}