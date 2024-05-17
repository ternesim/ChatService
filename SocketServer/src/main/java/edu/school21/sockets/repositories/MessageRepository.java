package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;

import java.util.List;

public interface MessageRepository {
    void save(Message message);
    List<Message> findByRoomId(Long id);
}
