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

    List<Client> clients;

    public void start(int port) throws IOException {

        List<Client> clients = new ArrayList<>();
        ConnectionHandler connectionHandler = new ConnectionHandler(port, clients);
        connectionHandler.start();

        for (int i = 0; i < clients.size(); i++) {

            Client client = clients.get(i);
            JSONObject json = client.receiveRequest();
            RequestType requestType = json.optEnum(RequestType.class, "requestType", RequestType.UNDEFINED);

            User user;
            JSONObject responseJson = new JSONObject();
            switch (requestType) {

                case WELCOME:
                    responseJson.put("WelcomeRequest", "Hello from server");

                case SIGN_IN:
                    try {
                        user = usersService.signIn(json.getString("Login"), json.getString("Password"));
                        client.setUser(user);
                        responseJson.put("SignInResult", "Success");
                    } catch (AuthException e) {
                        responseJson.put("SignInResult", "Failure");
                    }
                    break;

                case SIGN_UP:
                    try {
                        user = usersService.signUp(json.getString("Login"), json.getString("Password"));
                        client.setUser(user);
                        responseJson.put("SignUpResult", "Success");
                    } catch (AuthException e) {
                        responseJson.put("SignUpResult", "Failure");
                    }
                    break;

                case ROOMS_LIST:
                    responseJson.put("RoomList", roomsService.getRoomList());

            }


        }
    }


}
