package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.domain.User;
import com.pragmatists.blog.events.domain.UserCreated;
import com.pragmatists.blog.events.domain.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserCreated(savedUser.id));
        return savedUser;
    }

}
