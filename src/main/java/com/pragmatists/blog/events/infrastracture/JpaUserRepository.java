package com.pragmatists.blog.events.infrastracture;

import com.pragmatists.blog.events.domain.User;
import com.pragmatists.blog.events.domain.UserId;
import org.springframework.data.repository.CrudRepository;

public interface JpaUserRepository extends CrudRepository<User, UserId> {
}
