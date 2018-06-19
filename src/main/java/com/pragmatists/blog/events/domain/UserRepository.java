package com.pragmatists.blog.events.domain;

public interface UserRepository {

    User find(UserId userId);
    User save(User user);
}
