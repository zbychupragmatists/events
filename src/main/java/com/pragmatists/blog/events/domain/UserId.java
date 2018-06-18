package com.pragmatists.blog.events.domain;

import java.io.Serializable;
import java.util.UUID;

public class UserId implements Serializable {
    UUID id;

    public UserId() {
        this.id = UUID.randomUUID();
    }

    public String asString() {
        return id.toString();
    }
}
