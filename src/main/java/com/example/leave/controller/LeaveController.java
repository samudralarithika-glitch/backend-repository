package com.example.leave.controller;

import com.example.leave.dto.LeaveRequestDTO;
import com.example.leave.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/apply/{studentId}")
    public ResponseEntity<LeaveRequestDTO.Response> applyLeave(
            @PathVariable Long studentId,
            @Valid @RequestBody LeaveRequestDTO.Request request) {
        LeaveRequestDTO.Response response = leaveService.applyLeave(studentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequestDTO.Response>> getAllLeaves(
            @RequestParam(required = false) String status) {
        if (status != null && !status.trim().isEmpty()) {
            return ResponseEntity.ok(leaveService.getLeavesByStatusString(status));
        }
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<LeaveRequestDTO.Response>> getPendingLeaves() {
        return ResponseEntity.ok(leaveService.getPendingLeaves());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<LeaveRequestDTO.Response>> getLeavesByStudent(
            @PathVariable Long studentId,
            @RequestParam(required = false) String status) {
        if (status != null && !status.trim().isEmpty()) {
            return ResponseEntity.ok(leaveService.getLeavesByStudentAndStatusString(studentId, status));
        }
        return ResponseEntity.ok(leaveService.getLeavesByStudent(studentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestDTO.Response> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<LeaveRequestDTO.Response> updateLeaveStatus(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestDTO.StatusUpdate statusUpdate) {
        return ResponseEntity.ok(leaveService.updateLeaveStatus(id, statusUpdate));
    }

    @PatchMapping("/{id}/cancel/{studentId}")
    public ResponseEntity<LeaveRequestDTO.Response> cancelLeave(
            @PathVariable Long id,
            @PathVariable Long studentId) {
        return ResponseEntity.ok(leaveService.cancelLeave(id, studentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
        Map<String, String> response = new HashMap<String, String>();
        response.put("message", "Leave deleted successfully");
        return ResponseEntity.ok(response);
    }
}