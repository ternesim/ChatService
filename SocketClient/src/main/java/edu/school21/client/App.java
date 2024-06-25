package edu.school21.client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class App {

    static BufferedReader in;

    enum AuthRequestType {
        WELCOME, SIGN_IN, SIGN_UP, EXIT, UNDEFINED
    }

    enum RequestType {
        CREATE_ROOM, ROOMS_LIST, JOIN_ROOM, MESSAGE, EXIT, UNDEFINED
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port;
        try {
            if (args.length != 1 || !args[0].startsWith("--server-port=")) throw new IllegalArgumentException();
            port = Integer.parseInt(args[0].replace("--server-port=", ""));
        } catch (Exception e) {
            System.out.println("Port parsing error");
            return;
        }

        Socket clientSocket;
        try {
            clientSocket = new Socket("localhost", 6667);
        } catch (ConnectException e) {
            System.out.println("Server is not up");
            return;
        }


        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Thread.sleep(100);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestType", AuthRequestType.WELCOME);
        out.println(jsonObject);


//        Listener listener = new Listener(in);
//        listener.start();


        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("requestType", AuthRequestType.WELCOME);
        out.println(jsonObject2);

        JSONObject jsonObject1 = new JSONObject(in.readLine());
        System.out.println(getServerResponse().get("message"));


//
//        JSONObject hello = new JSONObject(in.readLine());
//        System.out.println(hello.get("message"));


        Scanner scanner = new Scanner(System.in);
        //System.out.println("\t1. Create room\n\t2. Choose room\n\t3. Exit");
        System.out.println("1. signIn\n2. SignUp\n3. Exit");

        int option1 = scanner.nextInt();
        if (option1 == 3) System.exit(0);
        AuthRequestType authRequestType = AuthRequestType.values()[option1];

        System.out.println(authRequestType);

        switch (authRequestType) {
            case SIGN_IN:
                JSONObject jsonObject3 = new JSONObject();
                jsonObject3.put("requestType", authRequestType);
                System.out.println("Enter username:");
                jsonObject3.put("login", scanner.next());
                System.out.println("Enter password:");
                jsonObject3.put("password", scanner.next());
                out.println(jsonObject3);

                JSONObject j = getServerResponse();
                if (!j.getString("result").equals("Success")) {
                    throw new RuntimeException("auth not passed");
                }
                break;

            case SIGN_UP:
                break;

            case EXIT:
                return;

            default:
                System.err.println("unappropriated request type");
                throw new RuntimeException("unappropriated request type");
        }

        System.out.println("\t1. Create room\n\t2. Choose room\n\t3. Exit");
        int option2 = scanner.nextInt();
        if (option2 == 3) return;
        RequestType authRequestType1 = RequestType.values()[option2 - 1];
        System.out.println(authRequestType1);
        System.out.println(option2);
        System.out.println(Arrays.toString(AuthRequestType.values()));
        JSONObject jsonObject3 = new JSONObject();
        switch (authRequestType1) {
            case ROOMS_LIST:
                jsonObject3.put("requestType", RequestType.ROOMS_LIST);
                out.println(jsonObject3);
                JSONObject response = getServerResponse();

                System.out.println(response);

                JSONArray list = response.getJSONArray("roomsList");
                System.out.println("Rooms:");
                int count = 1;
                for (Object name : list) {
                    System.out.println(count++ + ". " + name);
                }
                System.out.println(count + ". Exit");

                int option = scanner.nextInt();
                if (option == count) System.exit(0);

                JSONObject joinRoomRequest = new JSONObject();
                joinRoomRequest.put("requestType", RequestType.JOIN_ROOM);
                joinRoomRequest.put("name", list.get(option - 1));

                out.println(joinRoomRequest);

                new Listener(in).start();

                String message = "";
                while (!message.equals("Exit")) {
                    message = scanner.next();
                    out.println(message);
                }
                System.exit(0);
                break;


            case CREATE_ROOM:
                jsonObject3.put("requestType", RequestType.CREATE_ROOM);
                break;

            case EXIT:
                return;

            default:
                System.err.println("unappropriated request type");
                throw new RuntimeException("unappropriated request type");
        }


    }


    static JSONObject getServerResponse() throws IOException {
        return new JSONObject(in.readLine());
    }

}
        //check it is in range


//
//        try {
//            String input;
//            while (!clientSocket.isClosed()) {
//                input = scanner.nextLine();
//
//                Message message = new Message(listener.getLogin(), input, listener.getRoomId());
//                String json = objectMapper.writeValueAsString(message);
//                out.println(json);
//                if (input.equals("Exit")) {
//                    System.out.println("You have left the chat.");
//                    break;
//                }
//            }
//        } catch (Exception ignored) {
//        } finally {
//            scanner.close();
//            listener.interrupt();
//            clientSocket.close();
//            in.close();
//            out.close();
//        }
//    }
//
//}