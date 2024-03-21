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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
    private JwtUtils jwtUtils;
    @GetMapping
    public ResponseEntity<?> getInfoAdmin(@RequestHeader("Authorization") String authHeader){

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
    @GetMapping("/requests")
    public ResponseEntity<?> getLeaveRequests(){
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        if (leaveRequests.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Danh sách request rỗng"));
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
            resp.setEmail(leaveRequest.getUser().getEmail());
            resp.setReason(leaveRequest.getReason());
            resp.setStatus(leaveRequest.getStatus());

            leaveRequestList.add(resp);
        }

        if (leaveRequestList.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Danh sách request rỗng"));
        }
        return ResponseEntity.ok(leaveRequestList);
    }

    @GetMapping("/requests/process")
    public ResponseEntity<?> getLeaveRequestsInQueue(){
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        if (leaveRequests.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Danh sách request rỗng."));
        }

        //Get data
        List<LeaveResponse> leaveRequestList = new ArrayList<>();
        for (LeaveRequest leaveRequest: leaveRequests){
            if (leaveRequest.getStatus() == EStatus.PROCESS){

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
        }

        if (leaveRequestList.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Danh sách request rỗng"));
        }

        return ResponseEntity.ok(leaveRequestList);
    }

    @GetMapping("/requests/accept")
    public ResponseEntity<?> getLeaveRequestsAccept(){
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        if (leaveRequests.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Danh sách request rỗng."));
        }

        //Get data
        List<LeaveResponse> leaveRequestList = new ArrayList<>();
        for (LeaveRequest leaveRequest: leaveRequests){
            if (leaveRequest.getStatus() == EStatus.ACCEPT){

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
        }

        if (leaveRequestList.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Danh sách request rỗng"));
        }

        return ResponseEntity.ok(leaveRequestList);
    }

    @GetMapping("/requests/reject")
    public ResponseEntity<?> getLeaveRequestsReject(){
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        if (leaveRequests.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Danh sách request rỗng."));
        }

        //Get data
        List<LeaveResponse> leaveRequestList = new ArrayList<>();
        for (LeaveRequest leaveRequest: leaveRequests){
            if (leaveRequest.getStatus() == EStatus.REJECT){

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
        }

        if (leaveRequestList.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Danh sách request rỗng"));
        }

        return ResponseEntity.ok(leaveRequestList);
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
            User user = leaveRequest.getUser();

            //Lấy user và thực hiện update remaning time
            Date start = leaveRequest.getStartDate();
            Date end = leaveRequest.getEndDate();

            Long diff = Math.abs(start.getTime() - end.getTime()) / 86400000;

            //Khoảng tgian nghỉ người dùng cho phép
            if (user.getRemainingLeaveDays() >= diff){

                leaveRequest.setStatus(EStatus.ACCEPT);
                user.setRemainingLeaveDays(user.getRemainingLeaveDays() - diff);
                userRepository.save(user);
            } else {

                leaveRequest.setStatus(EStatus.REJECT);
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(new MessageResponse("Số ngày nghỉ vượt quá giới hạn."));
            }

        } else leaveRequest.setStatus(EStatus.REJECT);

        leaveRequestRepository.save(leaveRequest);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new MessageResponse("Cập nhật thành công."));
    }
}
