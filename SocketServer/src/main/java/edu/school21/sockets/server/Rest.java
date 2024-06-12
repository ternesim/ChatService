package edu.school21.sockets.server;


import edu.school21.sockets.models.User;
import edu.school21.sockets.services.RoomsService;
import edu.school21.sockets.services.UsersService;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Rest {

    UsersService usersService;
    RoomsService roomsService;

    enum RequestType {
        WELCOME, SIGN_IN, SIGN_UP, ROOMS_LIST, UNDEFINED
    }

    enum Result {
        SUCCESS, FAILURE
    }

    List<Client> clients;

    public void start(int port) throws IOException {

        List<Client> clients = new ArrayList<>();
        ConnectionHandler connectionHandler = new ConnectionHandler(port, clients);
        connectionHandler.start();

        while (true) {

            System.out.println("while Clients size " + clients.size());



            for (int i = 0; i < clients.size(); i++) {


                Client client = clients.get(i);

                System.out.println("before receive");

                JSONObject json = client.receiveRequest();
                System.out.println("From client:");
                System.out.println("From client: " + json);
                RequestType requestType = json.optEnum(RequestType.class, "requestType", RequestType.UNDEFINED);

                User user;
                Result result;
                JSONObject responseJson = new JSONObject();
                switch (requestType) {

                    case WELCOME:
                        responseJson.put("Response", "Hello from server");

                    case SIGN_IN:
                        try {
                            user = usersService.signIn(json.getString("Login"), json.getString("Password"));
                            client.setUser(user);
                            responseJson.put("Result", "Success");
                        } catch (AuthException e) {
                            responseJson.put("Result", "Failure");
                            responseJson.put("Message", e.getMessage());
                        }
                        break;

                    case SIGN_UP:
                        try {
                            user = usersService.signUp(json.getString("Login"), json.getString("Password"));
                            client.setUser(user);
                            responseJson.put("Result", "Success");
                        } catch (AuthException e) {
                            responseJson.put("Result", "Failure");
                        }
                        break;

                    case ROOMS_LIST:
                        responseJson.put("Result", "Success");
                        responseJson.put("RoomList", roomsService.getRoomList());
                        break;

                    case UNDEFINED:
                        System.err.println("Undefined request type");
                        break;

                    default:
                        System.err.println("The request type was not processed.");

                }

                client.send(responseJson);

            }
        }
    }


}
