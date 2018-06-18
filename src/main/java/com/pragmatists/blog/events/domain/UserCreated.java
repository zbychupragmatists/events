package com.pragmatists.blog.events.domain;

public class UserCreated {
    public UserId id;

    public UserCreated(UserId id) {
        this.id = id;
    }
}
