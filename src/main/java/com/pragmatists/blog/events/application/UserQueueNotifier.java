package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.domain.EmailWithToken;
import com.pragmatists.blog.events.domain.User;
import com.pragmatists.blog.events.domain.UserCreated;
import com.pragmatists.blog.events.domain.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserQueueNotifier {

    private final JmsTemplate jmsTemplate;
    private final UserRepository userRepository;

    public UserQueueNotifier(JmsTemplate jmsTemplate, UserRepository userRepository) {
        this.jmsTemplate = jmsTemplate;
        this.userRepository = userRepository;
    }

    @EventListener(UserCreated.class)
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // uncomment to make pass tests in UserResourceJdbcTemplateTest
//    @Transactional(propagation = Propagation.REQUIRES_NEW) // uncomment to make pass method
//    com.pragmatists.blog.events.application.UserResourceTest#registeredUserHasTokenGenerated
    public void onUserCreate(UserCreated userCreated) {
        User user = userRepository.find(userCreated.id);
        user.generateToken();
        userRepository.save(user);
        jmsTemplate.convertAndSend("emails", new EmailWithToken(user.email, user.token()).asJmsMessage());
    }
}
