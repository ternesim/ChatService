package edu.school21.sockets.services;

import edu.school21.sockets.models.Room;
import edu.school21.sockets.server.Client;

import java.util.List;

public interface RoomsService {
    Room findRoom(long id);
    Room findRoom(String name);

    Room createRoom(String name, Client client);

    List<Room> getRoomList();
}
