package edu.school21.sockets.services;

import edu.school21.sockets.models.Room;
import edu.school21.sockets.repositories.MessageRepository;
import edu.school21.sockets.repositories.RoomsRepository;
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
        return null;
    }

    @Override
    public List<Room> getRoomList() {
        return roomsRepository.findAll();
    }
}
