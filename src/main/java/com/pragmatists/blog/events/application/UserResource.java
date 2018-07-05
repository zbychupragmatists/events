package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserJson registerUser(@RequestBody CreateUserJson createUserJson) {
        User savedUser = userService.createUser(createUserJson.asUser());
        return savedUser.asJson();
    }

//    public UserJson

}
