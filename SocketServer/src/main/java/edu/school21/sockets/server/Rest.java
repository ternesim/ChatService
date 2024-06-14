package edu.school21.sockets.server;


import edu.school21.sockets.models.User;
import edu.school21.sockets.services.RoomsService;
import edu.school21.sockets.services.UsersService;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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

        List<Client> clients = Collections.synchronizedList(new ArrayList<>());
        ConnectionHandler connectionHandler = new ConnectionHandler(port, clients);
        connectionHandler.start();

        //System.out.println("Rest before while");
        //System.out.println("before while Clients size " + clients.size());

        while (true) {

            synchronized (clients) {
                for (Client client : clients) {

                    //System.out.println("before receive");

                    JSONObject receivedJson = client.receiveRequest();
                    System.out.println("From client:");
                    System.out.println("From client: " + receivedJson);
                    RequestType requestType = receivedJson.optEnum(
                            RequestType.class, "requestType", RequestType.UNDEFINED);
                    User user;
                    JSONObject responseJson = new JSONObject();

                    switch (requestType) {
                        case WELCOME:
                            responseJson.put("WelcomeMessage", "Hello from server");
                            break;

                        case SIGN_IN:
                            String signLogin = receivedJson.getString("Login");
                            String signPass = receivedJson.getString("Password");
                            User signedUser = usersService.signIn(signLogin, signPass);
                            client.setUser(signedUser);
                            break;

                        case SIGN_UP:
                            String newLogin = receivedJson.getString("Login");
                            String newPass = receivedJson.getString("Password");
                            User newUser = usersService.signUp(newLogin, newPass);
                            client.setUser(newUser);
                            break;

                        case ROOMS_LIST:
                            //if (client.isAuthenticated()) {
                            if (true) {
                                onSuccess(responseJson);
                                //responseJson.put("List", roomsService.getRoomList());
                                String[] l = new String[]{"AA", "BB"};
                                responseJson.put("List", l);
                            } else {
                                onFailure(responseJson, "User not authenticated");
                            }
                            break;

                        case UNDEFINED:
                            String messageForUndefined = "Undefined request type";
                            System.err.println(messageForUndefined);
                            onFailure(responseJson, messageForUndefined);
                            break;

                        default:
                            String messageForDefault = "Internal server error";
                            System.err.println(messageForDefault);
                            onFailure(responseJson, messageForDefault);
                        }

                    client.send(responseJson);

                }
            }
        }
    }

     void signIn(JSONObject receivedJson, Client client) {
        try {
            User user = usersService.signIn(
                    receivedJson.getString("Login"),
                    receivedJson.getString("Password"));
            client.setUser(user);
        } catch (AuthException e) {
            onFailure(receivedJson, e.getMessage());
        }
    }

    void onFailure(JSONObject json, String message) {
        json.put("Result", "Failure");
        json.put("Message", message);
    }

    void onSuccess(JSONObject json) {
        json.put("Result", "Success");
    }

}
