package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = jdbcTemplate.queryForObject(
                "SELECT id, login, password FROM \"user\" WHERE id=?",
                (resultSet, rowNum) -> new User(
                        resultSet.getLong("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password")
                ), id);
        return user == null ? Optional.empty() : Optional.of(user);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "SELECT id, login, password FROM \"user\"",
                (resultSet, rowNum) -> new User(
                        resultSet.getLong("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password")
                ));
    }

    @Override
    public void save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO \"user\" (login, password) VALUES (?, ?)", new String[]{"id"});
                    ps.setString(1, entity.getLogin());
                    ps.setString(2, entity.getPassword());
                    return ps;
                },
                keyHolder
        );
        entity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update(
                "UPDATE \"user\" SET id=?, login=?, password=? WHERE id=?",
                entity.getId(),
                entity.getLogin(),
                entity.getPassword()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM \"user\" WHERE id=?", id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Optional<User> optionalUser = Optional.empty();
        try {
            User user = jdbcTemplate.queryForObject(
                    "SELECT id, login, password FROM \"user\" WHERE login=?",
                    (resultSet, rowNum) -> new User(
                            resultSet.getLong("id"),
                            resultSet.getString("login"),
                            resultSet.getString("password")
                    ), login);
            optionalUser = Optional.of(user);
        } catch (EmptyResultDataAccessException ignored) {}
        return optionalUser;
    }
}
