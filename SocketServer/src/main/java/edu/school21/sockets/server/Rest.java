package edu.school21.sockets.server;


import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import edu.school21.sockets.server.expetions.AuthException;
import edu.school21.sockets.services.RoomsService;
import edu.school21.sockets.services.UsersService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Rest {
    UsersService usersService;
    RoomsService roomsService;

    @Autowired
    public Rest(UsersService usersService, RoomsService roomsService) {
        this.usersService = usersService;
        this.roomsService = roomsService;
    }

    public void start(int port) throws IOException {

        System.out.println("Server started");

        List<Client> clients = new CopyOnWriteArrayList<>();
        ConnectionHandler connectionHandler = new ConnectionHandler(port, clients);
        connectionHandler.start();

        while (true) {
            for (Client client : clients) {
                try {
                    JSONObject requestJson = client.receiveRequest();
                    System.out.println("From client:");
                    System.out.println("From client: " + requestJson);
                    RequestType requestType = requestJson.optEnum(
                            RequestType.class, "requestType", RequestType.UNDEFINED);
                    JSONObject responseJson = new JSONObject();

                    switch (requestType) {
                        case WELCOME:
                            responseJson.put("message", "Hello from server");
                            break;

                        case SIGN_IN:
                            String signLogin = requestJson.getString("login");
                            String signPass = requestJson.getString("password");
                            User signedUser = usersService.signIn(signLogin, signPass);
                            client.setUser(signedUser);
                            break;

                        case SIGN_UP:
                            String newLogin = requestJson.getString("login");
                            String newPass = requestJson.getString("password");
                            User newUser = usersService.signUp(newLogin, newPass);
                            client.setUser(newUser);
                            break;

                        case ROOMS_LIST:
                            //if (client.isAuthenticated()) {
                            if (false) throw new AuthException("User is not authenticated");
                            List<Room> roomList = roomsService.getRoomList();
                            String[] names = roomList.stream().map(Room::getName).toArray(String[]::new);
                            responseJson.put("roomsList", names);
                            break;

                        case JOIN_ROOM:
                            if (false) throw new AuthException("User is not authenticated");
                            String roomName = requestJson.getString("name");
                            Room room = roomsService.findRoom(roomName);
                            responseJson.put("messagesList", room.getLastMessages());
                            client.setRoom(room);
                            break;

                        case CREATE_ROOM:
                            if (true) throw new AuthException("User is not authenticated");
                            String name = requestJson.getString("name");
                            Room newRoom = roomsService.createRoom(name, client);
                            client.setRoom(newRoom);
                            break;

                        case MESSAGE:
                            if (true) throw new AuthException("User is not authenticated");
                            String message = requestJson.getString("message");
                            JSONObject broadcast = new JSONObject();
                            broadcast.put("chatMessage", message);
                            for (Client chatter : clients) {
                                if (!chatter.equals(client)
                                        && chatter.getRoom().equals(client.getRoom())) {
                                    chatter.send(broadcast);
                                }
                            }
                            break;

                        case EXIT:
                            client.closeConnection();
                            clients.remove(client);
                            break;

                        case UNDEFINED:
                            throw new Exception("Undefined request type");

                        default:
                            throw new Exception("Internal server error");
                    }
                    responseJson.put("result", "Success");
                    client.send(responseJson);
                } catch (IOException e) {
                    e.printStackTrace();
                    client.closeConnection();
                    clients.remove(client);
                    System.err.println(client.getSocket() + " closed with error");
                } catch (Exception e) {
                    e.printStackTrace();
                    JSONObject json = new JSONObject();
                    json.put("result", "Failure");
                    json.put("message", e.getMessage());
                    client.send(json);
                    client.closeConnection();
                    clients.remove(client);
                }
            }
        }
    }

    enum RequestType {
        WELCOME, SIGN_IN, SIGN_UP, ROOMS_LIST, JOIN_ROOM, MESSAGE, EXIT, CREATE_ROOM, UNDEFINED
    }
}
