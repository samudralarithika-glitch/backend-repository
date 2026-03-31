package com.example.leave.service;

import com.example.leave.dto.LeaveRequestDTO;
import java.util.List;

public interface LeaveService {
    LeaveRequestDTO.Response applyLeave(Long studentId, LeaveRequestDTO.Request request);
    LeaveRequestDTO.Response getLeaveById(Long leaveId);
    List<LeaveRequestDTO.Response> getAllLeaves();
    List<LeaveRequestDTO.Response> getLeavesByStudent(Long studentId);
    List<LeaveRequestDTO.Response> getLeavesByStatusString(String status);
    List<LeaveRequestDTO.Response> getPendingLeaves();
    List<LeaveRequestDTO.Response> getLeavesByStudentAndStatusString(Long studentId, String status);
    LeaveRequestDTO.Response updateLeaveStatus(Long leaveId, LeaveRequestDTO.StatusUpdate statusUpdate);
    LeaveRequestDTO.Response cancelLeave(Long leaveId, Long studentId);
    void deleteLeave(Long leaveId);
}