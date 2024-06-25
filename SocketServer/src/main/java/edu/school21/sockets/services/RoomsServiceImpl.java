package edu.school21.sockets.services;

import edu.school21.sockets.models.Room;
import edu.school21.sockets.repositories.MessageRepository;
import edu.school21.sockets.repositories.RoomsRepository;
import edu.school21.sockets.server.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomsServiceImpl implements RoomsService {

    RoomsRepository roomsRepository;
    MessageRepository messageRepository;

    @Autowired
    public RoomsServiceImpl(RoomsRepository rp, MessageRepository mr) {
        this.roomsRepository = rp;
        this.messageRepository = mr;
    }

    @Override
    public Room findRoom(long id) {
        return roomsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No room exist with given id"));
    }

    @Override
    public Room findRoom(String name) {
        return roomsRepository.findByName(name).orElseThrow(
                () -> new RuntimeException("No room exist with given name"));
    }

    @Override
    public Room createRoom(String name, Client client) {
        roomsRepository.findByName(name).ifPresent(s -> {
            throw new RuntimeException("Room with this name already exist");
        });
        Room room = new Room(0L, name, client.getUser().getId());
        roomsRepository.save(room);
        return room;
    }

    @Override
    public List<Room> getRoomList() {
        return roomsRepository.findAll();
    }
}
