package com.miratech.miratechtechtask.dto;

public enum TaskStatus {
    PENDING("pending"),
    COMPLETED("completed"),
    IN_PROGRESS("in progress"),
    TO_DO("to do");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

}
