package com.example.management.security.services;

import com.example.management.entity.LeaveRequest;
import com.example.management.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LeaveRequestService {

    @Autowired
    LeaveRequestRepository leaveRequestRepository;

    public boolean checkDuplicateRequest(Date start, Date end, Long userId) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.getLeaveRequestsByUser_Id(userId);
        for (LeaveRequest leaveRequest : leaveRequests) {
            if ((start.compareTo(leaveRequest.getStartDate()) >= 0 && start.compareTo(leaveRequest.getEndDate()) <= 0) ||
                    (end.compareTo(leaveRequest.getStartDate()) >= 0 && end.compareTo(leaveRequest.getEndDate()) <= 0)) {
                return true;
            }
        }
        return false;
    }
}
