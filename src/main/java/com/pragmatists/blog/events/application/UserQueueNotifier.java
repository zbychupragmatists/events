package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.domain.EmailWithToken;
import com.pragmatists.blog.events.domain.User;
import com.pragmatists.blog.events.domain.UserCreated;
import com.pragmatists.blog.events.infrastracture.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        EmailToken emailToken = new EmailToken();
        Optional<User> userOptional = userRepository.findById(userCreated.id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            jmsTemplate.convertAndSend("emails", new EmailWithToken(user.email, emailToken.asString()).asJmsMessage());
            user.tokenSend(emailToken);
            userRepository.save(user);
        }
    }
}
