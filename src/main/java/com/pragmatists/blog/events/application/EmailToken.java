package com.pragmatists.blog.events.application;

import java.util.UUID;

public class EmailToken {

    private final String token;

    public EmailToken() {
        token = UUID.randomUUID().toString();
    }

    public String asString() {
        return token;
    }
}
