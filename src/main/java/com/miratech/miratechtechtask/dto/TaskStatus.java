package com.miratech.miratechtechtask.dto;

import lombok.Getter;

@Getter
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

    public static TaskStatus fromStatus(String status) {
        for (TaskStatus enumValue : values()) {
            if (enumValue.getStatus().equalsIgnoreCase(status)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("No enum constant with status: " + status);
    }

}
