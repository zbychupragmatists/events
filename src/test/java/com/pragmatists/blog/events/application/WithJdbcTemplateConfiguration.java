package com.pragmatists.blog.events.application;

import com.pragmatists.blog.events.domain.UserRepository;
import com.pragmatists.blog.events.infrastracture.JdbcTemplateUserRepository;
import com.pragmatists.blog.events.infrastracture.JpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class WithJdbcTemplateConfiguration {

    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate, JpaUserRepository jpaUserRepository) {
        return new JdbcTemplateUserRepository(jdbcTemplate, jpaUserRepository);
    }
}
