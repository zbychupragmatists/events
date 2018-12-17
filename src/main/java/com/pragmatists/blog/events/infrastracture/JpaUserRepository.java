package com.pragmatists.blog.events.infrastracture;

import com.pragmatists.blog.events.domain.User;
import com.pragmatists.blog.events.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, UserId> {
}
