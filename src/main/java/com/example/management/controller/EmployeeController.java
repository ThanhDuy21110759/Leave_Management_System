package com.example.management.controller;

import com.example.management.entity.EStatus;
import com.example.management.entity.LeaveRequest;
import com.example.management.entity.User;
import com.example.management.payload.response.LeaveResponse;
import com.example.management.payload.response.MessageResponse;
import com.example.management.payload.response.UserResponse;
import com.example.management.repository.LeaveRequestRepository;
import com.example.management.repository.UserRepository;
import com.example.management.security.jwt.JwtUtils;
import com.example.management.security.services.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
    private LeaveRequestService leaveRequestService;
    @GetMapping
    public ResponseEntity<?> getInfoEmployee(@RequestHeader("Authorization") String authHeader){

        // Get userId by Token (Header)
        String token = authHeader.replace("Bearer ", "").trim();

        Optional<User> userOptional = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
        if (userOptional.isPresent()) {
            //get data
            UserResponse user = new UserResponse();
            user.setId(userOptional.get().getId());
            user.setUsername(userOptional.get().getUsername());
            user.setEmail(userOptional.get().getEmail());
            user.setRemainingLeaveDays(userOptional.get().getRemainingLeaveDays());
            user.setRoles(userOptional.get().getRoles());

            return  ResponseEntity.ok()
                    .body(user);
        }
        return ResponseEntity.badRequest()
                .body(new MessageResponse("Không tìm thấy thông tin"));
    }

    @PostMapping("/request")
    public ResponseEntity<?> addNewLeaveRequest(@RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date start,
                                                @RequestParam("endDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date end,
                                                @RequestParam("reason") String reason,
                                                @RequestHeader("Authorization") String authHeader){

        String token = authHeader.replace("Bearer ", "").trim();

        // Get userId by Token (Header)
        Optional<User> userOptional = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
        if (userOptional.isPresent()){
            User user = userOptional.get();

            //Check trường hợp trùng request vs time > 12 vs remainingTime of User >= diff
            Long diff = Math.abs(start.getTime() - end.getTime()) / 86400000;

            if (user.getRemainingLeaveDays() >= diff && diff <= 12 && start.getTime() - end.getTime() < 0
                    && !leaveRequestService.checkDuplicateRequest(start, end, user.getId())){

                LeaveRequest newLeaveRequest = new LeaveRequest();
                newLeaveRequest.setStartDate(start);
                newLeaveRequest.setEndDate(end);
                newLeaveRequest.setReason(reason);
                newLeaveRequest.setStatus(EStatus.PROCESS);
                newLeaveRequest.setUser(user);
                leaveRequestRepository.save(newLeaveRequest);

                return ResponseEntity.ok()
                        .body(new MessageResponse("Thêm request thành công"));
            } else return ResponseEntity.badRequest()
                    .body(new MessageResponse("Request không hợp lệ"));

        } else return ResponseEntity.badRequest()
                .body(new MessageResponse("Login để thêm request"));
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
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Không tồn tại request"));
        }

        //check request trong qua trinh xu ly
        LeaveRequest leaveRequest = leaveRequestOpt.get();
        if (leaveRequest.getStatus() == EStatus.PROCESS){
            leaveRequestRepository.delete(leaveRequest);
        } else return ResponseEntity.badRequest()
                .body(new MessageResponse("Request đã được xử lý"));

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new MessageResponse("Xóa request thành công"));
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getLeaveRequest(@RequestHeader("Authorization") String authHeader){

        // Get userId by Token (Header)
        String token = authHeader.replace("Bearer ", "").trim();

        Optional<User> userOptional = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
        if (userOptional.isPresent()){
            Long userId = userOptional.get().getId();

            List<LeaveRequest> leaveRequests = leaveRequestRepository.getLeaveRequestsByUser_Id(userId);
            if (leaveRequests.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Không tìm thấy request từ " + userOptional.get().getUsername()));
            }

            //Get data
            List<LeaveResponse> leaveRequestList = new ArrayList<>();
            for (LeaveRequest leaveRequest: leaveRequests){

                //init items
                LeaveResponse resp = new LeaveResponse();
                resp.setRequestId(leaveRequest.getId());
                resp.setUsername(leaveRequest.getUser().getUsername());
                resp.setRemainingLeaveDays(leaveRequest.getUser().getRemainingLeaveDays());
                resp.setStartDate(leaveRequest.getStartDate());
                resp.setEndDate(leaveRequest.getEndDate());
                resp.setReason(leaveRequest.getReason());
                resp.setStatus(leaveRequest.getStatus());

                leaveRequestList.add(resp);
            }
            return ResponseEntity.ok(leaveRequestList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Vui lòng login"));
    }
}
