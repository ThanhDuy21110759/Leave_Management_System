package com.example.management.controller;

import com.example.management.entity.LeaveRequest;
import com.example.management.entity.User;
import com.example.management.repository.LeaveRequestRepository;
import com.example.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeController {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/request")
    public ResponseEntity<?> addNewLeaveRequest(@RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startDate,
                                                @RequestParam("endDate") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endDate,
                                                @RequestParam("reason") String reason){

        // Get userId by Token (Header)
        long userId = 1;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        LeaveRequest newLeaveRequest = new LeaveRequest();
        newLeaveRequest.setStartDate(startDate);
        newLeaveRequest.setEndDate(endDate);
        newLeaveRequest.setReason(reason);
        newLeaveRequest.setUser(user);
        leaveRequestRepository.save(newLeaveRequest);
        return ResponseEntity.ok("Leave request added successfully");
    }

    @PutMapping("/request")
    public ResponseEntity<?> updateLeaveRequest(@RequestParam("id") Long reqId,
                                                @RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startDate,
                                                @RequestParam("endDate") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endDate,
                                                @RequestParam("reason") String reason){
        Optional<LeaveRequest> leaveRequestOptional = leaveRequestRepository.findById(reqId);
        if (!leaveRequestOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
        }
        LeaveRequest leaveRequest = leaveRequestOptional.get();
        leaveRequest.setStartDate(startDate);
        leaveRequest.setEndDate(endDate);
        leaveRequest.setReason(reason);
        leaveRequestRepository.save(leaveRequest);
        return ResponseEntity.ok("Leave request updated successfully");
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getLeaveRequest(@RequestParam("id") Long id){
        List<LeaveRequest> leaveRequests = leaveRequestRepository.getLeaveRequestsByUser_Id(id);
        if (leaveRequests.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No requests found for this user");
        }
        return ResponseEntity.ok(leaveRequests);
    }
}
