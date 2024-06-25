package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Room;

import java.util.List;
import java.util.Optional;

public interface RoomsRepository {

    Optional<Room> findById(Long id);

    Optional<Room> findByName(String name);
    List<Room> findAll();
    void save(Room room);
}
