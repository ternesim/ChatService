package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public MessageRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Message message) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Messages (sender, text, room_id) VALUES (?, ?, ?)", new String[]{"id"});
            ps.setString(1, message.getSender());
            ps.setString(2, message.getText());
            ps.setLong(3, message.getRoomId());
            return ps;
        }, keyHolder);
        message.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public List<Message> findByRoomId(Long id) {
        return jdbcTemplate.query(
                "SELECT id, sender, room_id, text, timestamp FROM messages WHERE room_id = ?",
                new Object[]{id},
                (resultSet, rowNum) -> new Message(
                        resultSet.getLong("id"),
                        resultSet.getString("sender"),
                        resultSet.getString("text"),
                        resultSet.getLong("room_id"),
                        resultSet.getTimestamp("timestamp")
                )
        );
    }
}
