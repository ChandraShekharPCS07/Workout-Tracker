package com.workout.tracker.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
