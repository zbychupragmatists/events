package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.domain.UserId;

public class UserJson {
    public String id;
    public String login;
    public String email;
    public String emailToken;

    public UserJson(UserId id, String login, String email, String emailToken) {
        this.id = id.asString();
        this.login = login;
        this.email = email;
        this.emailToken = emailToken;
    }
}
