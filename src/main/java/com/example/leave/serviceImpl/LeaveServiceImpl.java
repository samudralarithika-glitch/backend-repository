package com.example.leave.serviceImpl;

import com.example.leave.dto.LeaveRequestDTO;
import com.example.leave.model.Leave;
import com.example.leave.model.Student;
import com.example.leave.repository.LeaveRepository;
import com.example.leave.repository.StudentRepository;
import com.example.leave.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public LeaveServiceImpl(LeaveRepository leaveRepository,
                            StudentRepository studentRepository) {
        this.leaveRepository = leaveRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public LeaveRequestDTO.Response applyLeave(Long studentId, LeaveRequestDTO.Request request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        List<Leave> overlapping = leaveRepository.findOverlappingLeaves(
                studentId, request.getStartDate(), request.getEndDate());
        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("Leave dates overlap with an existing application");
        }
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            throw new NoSuchElementException("Student not found with id: " + studentId);
        }
        Student student = studentOpt.get();
        Leave leave = new Leave();
        leave.setStudent(student);
        leave.setLeaveType(request.getLeaveType());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setReason(request.getReason());
        leave.setStatus(Leave.LeaveStatus.PENDING);
        return LeaveRequestDTO.Response.fromEntity(leaveRepository.save(leave));
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveRequestDTO.Response getLeaveById(Long leaveId) {
        return LeaveRequestDTO.Response.fromEntity(findById(leaveId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getAllLeaves() {
        List<Leave> leaves = leaveRepository.findAll();
        List<LeaveRequestDTO.Response> result = new ArrayList<LeaveRequestDTO.Response>();
        for (Leave leave : leaves) {
            result.add(LeaveRequestDTO.Response.fromEntity(leave));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getLeavesByStudent(Long studentId) {
        List<Leave> leaves = leaveRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
        List<LeaveRequestDTO.Response> result = new ArrayList<LeaveRequestDTO.Response>();
        for (Leave leave : leaves) {
            result.add(LeaveRequestDTO.Response.fromEntity(leave));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getLeavesByStatusString(String status) {
        Leave.LeaveStatus leaveStatus = Leave.LeaveStatus.valueOf(status.toUpperCase());
        List<Leave> leaves = leaveRepository.findByStatus(leaveStatus);
        List<LeaveRequestDTO.Response> result = new ArrayList<LeaveRequestDTO.Response>();
        for (Leave leave : leaves) {
            result.add(LeaveRequestDTO.Response.fromEntity(leave));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getPendingLeaves() {
        List<Leave> leaves = leaveRepository.findAllPendingLeaves(Leave.LeaveStatus.PENDING);
        List<LeaveRequestDTO.Response> result = new ArrayList<LeaveRequestDTO.Response>();
        for (Leave leave : leaves) {
            result.add(LeaveRequestDTO.Response.fromEntity(leave));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.Response> getLeavesByStudentAndStatusString(Long studentId,
                                                                             String status) {
        Leave.LeaveStatus leaveStatus = Leave.LeaveStatus.valueOf(status.toUpperCase());
        List<Leave> leaves = leaveRepository.findByStudentIdAndStatus(studentId, leaveStatus);
        List<LeaveRequestDTO.Response> result = new ArrayList<LeaveRequestDTO.Response>();
        for (Leave leave : leaves) {
            result.add(LeaveRequestDTO.Response.fromEntity(leave));
        }
        return result;
    }

    @Override
    public LeaveRequestDTO.Response updateLeaveStatus(Long leaveId,
                                                       LeaveRequestDTO.StatusUpdate statusUpdate) {
        Leave leave = findById(leaveId);
        if (leave.getStatus() == Leave.LeaveStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update a cancelled leave");
        }
        leave.setStatus(statusUpdate.getStatus());
        if (statusUpdate.getAdminRemarks() != null) {
            leave.setAdminRemarks(statusUpdate.getAdminRemarks());
        }
        return LeaveRequestDTO.Response.fromEntity(leaveRepository.save(leave));
    }

    @Override
    public LeaveRequestDTO.Response cancelLeave(Long leaveId, Long studentId) {
        Leave leave = findById(leaveId);
        if (!leave.getStudent().getId().equals(studentId)) {
            throw new SecurityException("Not authorized to cancel this leave");
        }
        if (leave.getStatus() == Leave.LeaveStatus.APPROVED) {
            throw new IllegalStateException("Cannot cancel an approved leave");
        }
        if (leave.getStatus() == Leave.LeaveStatus.CANCELLED) {
            throw new IllegalStateException("Leave is already cancelled");
        }
        leave.setStatus(Leave.LeaveStatus.CANCELLED);
        return LeaveRequestDTO.Response.fromEntity(leaveRepository.save(leave));
    }

    @Override
    public void deleteLeave(Long leaveId) {
        leaveRepository.delete(findById(leaveId));
    }

    private Leave findById(Long id) {
        Optional<Leave> opt = leaveRepository.findById(id);
        if (!opt.isPresent()) {
            throw new NoSuchElementException("Leave not found with id: " + id);
        }
        return opt.get();
    }
}