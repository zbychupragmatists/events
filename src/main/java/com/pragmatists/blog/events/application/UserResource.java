package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.domain.User;
import com.pragmatists.blog.events.domain.UserId;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserJson getUser(@PathVariable("id") String id) {
        return userService.getUser(new UserId(id)).asJson();
    }

    @PostMapping
    public UserJson registerUser(@RequestBody CreateUserJson createUserJson) {
        User savedUser = userService.createUser(createUserJson.asUser());
        return savedUser.asJson();
    }

}
