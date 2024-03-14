package com.example.management.controller;

import com.example.management.entity.EStatus;
import com.example.management.entity.LeaveRequest;
import com.example.management.entity.User;
import com.example.management.payload.response.MessageResponse;
import com.example.management.repository.LeaveRequestRepository;
import com.example.management.repository.UserRepository;
import com.example.management.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
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
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/request")
    public ResponseEntity<?> addNewLeaveRequest(@RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                                @RequestParam("endDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
                                                @RequestParam("reason") String reason,
                                                @RequestHeader("Authorization") String authHeader){

        String token = authHeader.replace("Bearer ", "").trim();

        // Get userId by Token (Header)
        Optional<User> userOptional = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
        if (userOptional.isPresent()){
            User user = userOptional.get();

            //Check trường hợp trùng request
            LeaveRequest newLeaveRequest = new LeaveRequest();
            newLeaveRequest.setStartDate(startDate);
            newLeaveRequest.setEndDate(endDate);
            newLeaveRequest.setReason(reason);
            newLeaveRequest.setStatus(EStatus.PROCESS);
            newLeaveRequest.setUser(user);
            leaveRequestRepository.save(newLeaveRequest);

            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("Leave request added successfully"));
        } else return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Login to add new request"));
    }

    @PutMapping("/request")
    public ResponseEntity<?> updateLeaveRequest(@RequestParam("id") Long reqId,
                                                @RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                                @RequestParam("endDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
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
        return ResponseEntity.ok().body(new MessageResponse("Leave request updated successfully"));
    }

    @DeleteMapping
    public ResponseEntity<?> removeLeaveRequest(@RequestParam("id") Long reqId){
        Optional<LeaveRequest> leaveRequestOpt = leaveRequestRepository.findById(reqId);
        if (!leaveRequestOpt.isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Không tồn tại request."));
        }

        //check request trong qua trinh xu ly
        LeaveRequest leaveRequest = leaveRequestOpt.get();
        if (leaveRequest.getStatus() == EStatus.PROCESS){

            leaveRequestRepository.delete(leaveRequest);
        } else return ResponseEntity.badRequest().body(new MessageResponse("Request đã được xử lý"));

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new MessageResponse("Xóa request thành công"));
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getLeaveRequest(@RequestParam("id") Long id){
        List<LeaveRequest> leaveRequests = leaveRequestRepository.getLeaveRequestsByUser_Id(id);
        if (leaveRequests.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("No requests found for this user"));
        }
        return ResponseEntity.ok(leaveRequests);
    }
}
