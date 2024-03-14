package com.example.management.controller;

import com.example.management.entity.EStatus;
import com.example.management.entity.LeaveRequest;
import com.example.management.entity.User;
import com.example.management.payload.response.MessageResponse;
import com.example.management.repository.LeaveRequestRepository;
import com.example.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/requests")
    public ResponseEntity<?> getLeaveRequests(){
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        if (leaveRequests.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Danh sách request rỗng."));
        }
        return ResponseEntity.ok(leaveRequests);
    }

    @PostMapping("/request")
    public ResponseEntity<?> setRequestStatus(@RequestParam("id") Long reqId,
                                              @RequestParam("status") Boolean status){

        Optional<LeaveRequest> leaveRequestOptional = leaveRequestRepository.findById(reqId);
        if (!leaveRequestOptional.isPresent()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy request");
        }

        LeaveRequest leaveRequest = leaveRequestOptional.get();
        if (status){
            leaveRequest.setStatus(EStatus.ACCEPT);
            User user = leaveRequest.getUser();

            //Lấy user và thực hiện update remaning time
            Date start = leaveRequest.getStartDate();
            Date end = leaveRequest.getEndDate();

            Long diff = Math.abs(start.getTime() - end.getTime()) / 86400000;

            //Khoảng tgian nghỉ người dùng cho phép
            if (user.getRemainingLeaveDays() >= diff){

                user.setRemainingLeaveDays(user.getRemainingLeaveDays() - diff);
                userRepository.save(user);
            } else return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new MessageResponse("Số ngày nghỉ vượt quá giới hạn."));

        } else leaveRequest.setStatus(EStatus.REJECT);

        leaveRequestRepository.save(leaveRequest);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new MessageResponse("Status updated successfully"));
    }
}
