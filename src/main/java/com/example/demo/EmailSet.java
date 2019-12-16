package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;

public class EmailSet {

    private String reasonText;

    private long userId;

    public EmailSet() {
    }

    public EmailSet(String reasonText) {
        this.reasonText = reasonText;
    }


    public EmailSet(long userId) {
        this.userId = userId;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}