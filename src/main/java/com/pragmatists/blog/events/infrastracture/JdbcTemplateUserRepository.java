package com.pragmatists.blog.events.infrastracture;

import com.pragmatists.blog.events.application.EmailToken;
import com.pragmatists.blog.events.domain.User;
import com.pragmatists.blog.events.domain.UserId;
import com.pragmatists.blog.events.domain.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplateUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JpaUserRepository jpaUserRepository;

    public JdbcTemplateUserRepository(JdbcTemplate jdbcTemplate, JpaUserRepository jpaUserRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User find(UserId userId) {
        return jdbcTemplate.queryForObject("SELECT * FROM USER WHERE ID = ?", new Object[]{userId.asString()}, new UserRowMapper());
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.saveAndFlush(user);
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            String id = rs.getString("id");
            String login = rs.getString("login");
            String email = rs.getString("email");
            String emailToken = rs.getString("email_token");
            User user = new User(new UserId(id), login, email);
            user.token(new EmailToken(emailToken));
            return user;
        }
    }

}
