package edu.school21.sockets.services;

import edu.school21.sockets.models.Room;

import java.util.List;

public interface RoomsService {
    Room findRoom(long id);

    List<Room> getRoomList();
}
