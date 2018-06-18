package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.domain.User;
import com.pragmatists.blog.events.infrastracture.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserResource {

    private final UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public UserJson registerUser(CreateUserJson userJson) {
        User savedUser = userRepository.save(userJson.asUser());
        return savedUser.asJson();
    }
}
