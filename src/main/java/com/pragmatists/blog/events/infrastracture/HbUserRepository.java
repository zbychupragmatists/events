package com.pragmatists.blog.events.infrastracture;

import com.pragmatists.blog.events.domain.User;
import com.pragmatists.blog.events.domain.UserId;
import com.pragmatists.blog.events.domain.UserRepository;

public class HbUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public HbUserRepository(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User find(UserId userId) {
        return jpaUserRepository.findById(userId).orElse(null);
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }
}
