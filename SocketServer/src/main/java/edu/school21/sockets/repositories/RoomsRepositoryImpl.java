package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RoomsRepositoryImpl implements RoomsRepository {
    JdbcTemplate jdbcTemplate;
    MessageRepository messageRepository;

    @Autowired
    public RoomsRepositoryImpl(DataSource dataSource, MessageRepository messageRepository) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.messageRepository = messageRepository;
    }

    @Override
    public Optional<Room> findById(Long id) {
        Room room = jdbcTemplate.queryForObject(
            "SELECT id, name, user_id FROM rooms WHERE id=?",
            (resultSet, rowNum) -> new Room(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getLong("user_id")), id);
        return room == null ? Optional.empty() : Optional.of(room);
    }

    @Override
    public Optional<Room> findByName(String name) {
        List<Room> list = jdbcTemplate.query(
                "SELECT * FROM rooms WHERE name = ?",
                (rs, i) -> new Room(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("user_id")),
                name);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = jdbcTemplate.query("SELECT id, name, user_id FROM rooms",
                (resultSet, rowNum) -> new Room(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getLong("user_id"))
        );
        for(Room room : rooms) {
            room.setMessageList(messageRepository.findByRoomId(room.getId()));
        }
        return rooms;
    }

    @Override
    public void save(Room entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO rooms (name, user_id) VALUES (?, ?)", new String[]{"id"});
                    ps.setString(1, entity.getName());
                    ps.setLong(2, entity.getCreatorId());
                    return ps;
                }, keyHolder);
        entity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }
}