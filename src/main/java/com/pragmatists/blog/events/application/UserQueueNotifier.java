package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.domain.EmailWithToken;
import com.pragmatists.blog.events.domain.User;
import com.pragmatists.blog.events.domain.UserCreated;
import com.pragmatists.blog.events.domain.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserQueueNotifier {

    private final JmsTemplate jmsTemplate;
    private final UserRepository userRepository;

    public UserQueueNotifier(JmsTemplate jmsTemplate, UserRepository userRepository) {
        this.jmsTemplate = jmsTemplate;
        this.userRepository = userRepository;
    }

    //REQUIRES_NEW is necessary to persist Database changes in @TransactionalEventListener, see here: https://docs.spring.io/autorepo/docs/spring/4.3.2.RELEASE/javadoc-api/org/springframework/transaction/support/TransactionSynchronization.html
    @EventListener(UserCreated.class)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onUserCreate(UserCreated userCreated) {
        User user = userRepository.find(userCreated.id);
        EmailToken emailToken = new EmailToken();
        user.token(emailToken);
        userRepository.save(user);
        jmsTemplate.convertAndSend("emails", new EmailWithToken(user.email, emailToken.asString()).asJmsMessage());
    }
}
