package com.example.management.repository;

import com.example.management.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    Optional<LeaveRequest> findById(Long id);
    List<LeaveRequest> getLeaveRequestsByUser_Id(Long id);
}
