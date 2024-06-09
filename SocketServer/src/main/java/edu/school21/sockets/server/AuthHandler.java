//package edu.school21.sockets.server;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import edu.school21.sockets.models.Message;
//import edu.school21.sockets.models.Room;
//import edu.school21.sockets.models.User;
//import edu.school21.sockets.repositories.RoomsRepository;
//import edu.school21.sockets.services.UsersService;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.sql.Timestamp;
//import java.util.List;
//
//public class AuthHandler extends Thread{
//    private final PrintWriter out;
//    private final BufferedReader in;
//    private final UsersService usersService;
//    private final RoomsRepository roomsRepository;
//    private final Socket socket;
//    private final ServerSocket serverSocket;
//    private final List<Client> clientList;
//    private final ObjectMapper objectMapper;
//
//    public AuthHandler(ConnectionHandler connectionHandler) throws IOException {
//        out = new PrintWriter(connectionHandler.getClientSocket().getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(connectionHandler.getClientSocket().getInputStream()));
//        this.usersService = connectionHandler.getUsersService();
//        this.clientList = connectionHandler.getClients();
//        this.socket = connectionHandler.getClientSocket();
//        this.serverSocket = connectionHandler.getServerSocket();
//        this.roomsRepository = connectionHandler.roomsRepository;
//        objectMapper = connectionHandler.objectMapper;
//    }
//
//    private void send(String text) {
//        String json;
//        try {
//            Message message = new Message(
//                    "server", text, 0L, new Timestamp(System.currentTimeMillis()));
//             json = objectMapper.writeValueAsString(message);
//             out.println(json);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new RuntimeException("message sending error");
//        }
//    }
//
//    private String receive() {
//        try {
//            String json = in.readLine();
//            Message message = objectMapper.readValue(json, Message.class);
//            return message.getText();
//        } catch (Exception e) {
//            throw new RuntimeException("message receiving error");
//        }
//    }
//
//    private void exit() {
//        try {
//            send("You have left the chat.");
//            socket.close();
//            in.close();
//            out.close();
//            System.out.println("Connection closed " + socket);
//        } catch (IOException e) {
//            System.err.println("Something went wrong during closing");
//        }
//    }
//
//    private void signUp() {
//        send("Enter username:");
//        String login = receive();
//        send("Enter password:");
//        String password = receive();
//        if (usersService.signUp(login, password)) {
//            send("Successful!");
//        } else {
//            send("Login occupied");
//        }
//    }
//
//    private boolean authorizeUser(User user) {
//        send("Enter username:");
//        String login = receive();
//        send("Enter password:");
//        String password = receive();
//        user.setLogin(login);
//        user.setPassword(password);
//        send("!!!LOGIN!!!" + login);
//        if (!usersService.signIn(user)) {
//            send("Wrong credentials");
//            return true;
//        }
//        return false;
//    }
//
//    private void chooseRoom(User user) {
//        send("Rooms");
//        int i = 1;
//
//        List<Room> roomList;
//        roomList = roomsRepository.findAll();
//        for(Room room : roomList) {
//            send(i++ + ". " + room.getName());
//        }
//        send(i + ". Exit");
//
//        int roomNum;
//        try {
//            roomNum = Integer.parseInt(receive()) - 1;
//        } catch (Exception e) {
//            throw new RuntimeException("Room number parsing error");
//        }
//
//        if (roomNum == i - 1) {
//            exit();
//            return;
//        }
//
//        if (roomNum > 0 && roomNum < roomList.size()) {
//            Room room = roomList.get(roomNum);
//            send("!!!roomId!!!" + room.getId());
//            send(room.getName() + " --");
//            int start = Math.max(room.getMessageList().size() - 30, 0);
//            for (int j = start; j < room.getMessageList().size(); j++) {
//                Message message = room.getMessageList().get(j);
//                send(message.getSender() + ": " + message.getText());
//            }
//            clientList.add(
//                    new Client(socket, out, in, user.getLogin(),
//                            roomList.get(roomNum).getId(), objectMapper));
//              if (serverSocket.isClosed()) {
//                  send("Server has been stopped");
//                  throw new RuntimeException("Server was stopped");
//              }
//        } else {
//            throw new RuntimeException("Incorrect room number");
//        }
//    }
//
//    private void createRoom(User user) {
//        send("Enter room name");
//        String name = receive();
//        List<Room> roomList = roomsRepository.findAll();
//        if (roomList.stream().anyMatch(room -> room.getName().equals(name))) {
//            send("Room already exist");
//        } else{
//            Room room = new Room(0L, name, user.getId());
//            roomsRepository.save(room);
//            send("Room created");
//            clientList.add(
//                    new Client(socket, out, in, user.getLogin(), room.getId(), objectMapper));
//        }
//    }
//
//    private void signIn() {
//        while (true) {
//            User user = new User();
//            if (authorizeUser(user)) continue;
//            send("\t1. Create room\n\t2. Choose room\n\t3. Exit");
//            switch (receive()) {
//                case "1":
//                    createRoom(user);
//                    break;
//                case "2":
//                    chooseRoom(user);
//                    break;
//                case "3":
//                    exit();
//                    break;
//                default:
//                    send("Incorrect input");
//                    continue;
//            }
//            break;
//        }
//    }
//
//    @Override
//    public void run() {
//        send("Hello from Server!");
//        while (true) {
//            try {
//                send("1. signIn\n2. SignUp\n3. Exit");
//                switch (receive()) {
//                    case "1":
//                        signIn();
//                        break;
//                    case "2":
//                        signUp();
//                        continue;
//                    case "3":
//                        exit();
//                        break;
//                    default:
//                        send("Incorrect input");
//                        continue;
//                }
//            } catch (Exception e) {
//                System.out.println("Connection closed with error " + socket);
//                exit();
//            }
//            break;
//        }
//    }
//}