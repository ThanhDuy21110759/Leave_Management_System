package com.example.management.controller;

import com.example.management.entity.EStatus;
import com.example.management.entity.LeaveRequest;
import com.example.management.payload.response.MessageResponse;
import com.example.management.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @GetMapping("/requests")
    public ResponseEntity<?> getLeaveRequests(){
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        return ResponseEntity.ok(leaveRequests);
    }

    @PostMapping("/request")
    public ResponseEntity<?> setRequestStatus(@RequestParam("id") Long reqId,
                                              @RequestParam("status") Boolean status){

        Optional<LeaveRequest> leaveRequestOptional = leaveRequestRepository.findById(reqId);
        if (!leaveRequestOptional.isPresent()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Request not found.");
        }

        LeaveRequest leaveRequest = leaveRequestOptional.get();
        if (status){
            leaveRequest.setStatus(EStatus.ACCEPT);
        } else leaveRequest.setStatus(EStatus.REJECT);

        leaveRequestRepository.save(leaveRequest);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new MessageResponse("Status updated successfully"));
    }
}
