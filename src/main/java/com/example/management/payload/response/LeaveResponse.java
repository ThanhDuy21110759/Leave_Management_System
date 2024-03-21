package com.example.management.payload.response;

import com.example.management.entity.EStatus;

import java.util.Date;

public class LeaveResponse {
    private String username;
    private Long remainingLeaveDays;
    private Date startDate;
    private Long requestId;
    private Date endDate;
    private String reason;
    private String email;
    private EStatus status;

    public LeaveResponse() {

    }

    public LeaveResponse(String username, Long remainingLeaveDays, Date startDate, Long requestId, Date endDate, String reason, String email, EStatus status) {
        this.username = username;
        this.remainingLeaveDays = remainingLeaveDays;
        this.startDate = startDate;
        this.requestId = requestId;
        this.endDate = endDate;
        this.reason = reason;
        this.email = email;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRemainingLeaveDays() {
        return remainingLeaveDays;
    }

    public void setRemainingLeaveDays(Long remainingLeaveDays) {
        this.remainingLeaveDays = remainingLeaveDays;
    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long reqId) {
        this.requestId = reqId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
