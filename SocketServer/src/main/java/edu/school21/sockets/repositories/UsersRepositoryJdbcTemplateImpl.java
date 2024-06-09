package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        List<User> users = jdbcTemplate.query(
                "SELECT id, login, password FROM \"user\" WHERE id=?",
                (rs, i) -> new User(
                        rs.getLong("id"),
                        rs.getString("login"),
                        rs.getString("password")
                ), id);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "SELECT id, login, password FROM \"user\"",
                (resultSet, rowNum) -> new User(
                        resultSet.getLong("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password")));
    }

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO \"user\" (login, password) VALUES (?, ?)", new String[]{"id"});
                    ps.setString(1, user.getLogin());
                    ps.setString(2, user.getPassword());
                    return ps;
                }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(
                "UPDATE \"user\" SET id=?, login=?, password=? WHERE id=?",
                user.getId(),
                user.getLogin(),
                user.getPassword());
        return user;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM \"user\" WHERE id=?", id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM \"user\" WHERE id = ?",
                (rs, i) -> new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ), login);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}
