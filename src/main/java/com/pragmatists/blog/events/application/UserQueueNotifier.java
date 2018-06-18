package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.domain.UserCreated;
import com.pragmatists.blog.events.infrastracture.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserQueueNotifier {

    private final JmsTemplate jmsTemplate;
    private final UserRepository userRepository;

    public UserQueueNotifier(JmsTemplate jmsTemplate, UserRepository userRepository) {
        this.jmsTemplate = jmsTemplate;
        this.userRepository = userRepository;
    }

    @EventListener(UserCreated.class)
    @Transactional
    public void onUserCreate(UserCreated userCreated) {
        userRepository.findById(userCreated.id)
                .ifPresent(u -> jmsTemplate.convertAndSend("emails", u.email));
    }
}
