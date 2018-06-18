package com.pragmatists.blog.events.infrastracture;

public interface EmailTemplate {
    void sendEmail(String email, String content);
}
