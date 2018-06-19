package com.pragmatists.blog.events.domain;

import java.io.Serializable;
import java.util.UUID;

public class UserId implements Serializable {
    String id;

    public UserId(String id) {
        this.id = id;
    }

    public UserId() {
        this.id = UUID.randomUUID().toString();
    }

    public String asString() {
        return id;
    }
}
