package com.pragmatists.blog.events.domain;

import com.pragmatists.blog.events.application.UserJson;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class User {

    @EmbeddedId
    public
    UserId id;
    String login;
    public String email;

    private User() { }

    public User(UserId id, String login, String email) {
        this.id = id;
        this.login = login;
        this.email = email;
    }

    public UserJson asJson() {
        return new UserJson(id, login, email);
    }
}
