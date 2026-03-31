package com.example.leave.repository;

import com.example.leave.model.Leave;
import com.example.leave.model.Leave.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    List<Leave> findByStudentId(Long studentId);

    List<Leave> findByStatus(LeaveStatus status);

    List<Leave> findByStudentIdAndStatus(Long studentId, LeaveStatus status);

    @Query("SELECT l FROM Leave l WHERE l.student.id = :studentId ORDER BY l.createdAt DESC")
    List<Leave> findByStudentIdOrderByCreatedAtDesc(@Param("studentId") Long studentId);

    @Query("SELECT l FROM Leave l WHERE l.student.id = :studentId AND l.startDate <= :endDate AND l.endDate >= :startDate")
    List<Leave> findOverlappingLeaves(@Param("studentId") Long studentId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(l) FROM Leave l WHERE l.student.id = :studentId AND l.status = :status")
    long countByStudentIdAndStatus(@Param("studentId") Long studentId,
                                   @Param("status") LeaveStatus status);

    @Query("SELECT l FROM Leave l WHERE l.status = :status ORDER BY l.createdAt ASC")
    List<Leave> findAllPendingLeaves(@Param("status") LeaveStatus status);
}