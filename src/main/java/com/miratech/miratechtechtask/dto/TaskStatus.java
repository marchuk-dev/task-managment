package com.miratech.miratechtechtask.dto;

public enum TaskStatus {
    PENDING("pending"),
    COMPLETED("completed"),
    IN_PROGRESS("in progress"),
    TO_DO("to do"),
    TESTING("in testing");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

}
