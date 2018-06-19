package com.pragmatists.blog.events.domain;

import java.io.Serializable;

import static java.lang.String.format;

public class EmailWithToken {
    private final String email;
    private final String token;

    public EmailWithToken(String email, String emailToken) {
        this.email = email;
        this.token = emailToken;
    }

    public String asJmsMessage() {
        return format("%s;%s", email, token);
    }
}
