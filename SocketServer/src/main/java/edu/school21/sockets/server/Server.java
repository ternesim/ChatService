//package edu.school21.sockets.server;
//
//import edu.school21.sockets.models.Message;
//import edu.school21.sockets.repositories.MessageRepository;
//import edu.school21.sockets.repositories.RoomsRepository;
//import edu.school21.sockets.services.UsersService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//@Component
//public class Server {
//    private final UsersService usersService;
//    private final MessageRepository messageRepository;
//    private final RoomsRepository roomsRepository;//??
//
//    @Autowired
//    public Server(UsersService usersService, MessageRepository messageRepository, RoomsRepository roomsRepository) {
//        this.usersService = usersService;
//        this.messageRepository = messageRepository;
//        this.roomsRepository = roomsRepository;
//    }
//
//    public void start(int port) throws IOException, InterruptedException {
//
//        List<Client> clients = new ArrayList<>();
//        ConnectionHandler connectionHandler = new ConnectionHandler(port, clients, usersService, roomsRepository);
//        connectionHandler.start();
//
//        BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
//        mainLoop:
//        while (true) {
//
//            while (inputStream.ready()) {
//                String cmd = inputStream.readLine();
//                if (cmd.equals("stop")) {
//                    connectionHandler.interrupt();
//                    for (Client client : clients) {
//                        client.sendMessage("Server has been stopped");
//                        client.close();
//                    }
//                    break mainLoop;
//                }
//            }
//
//            Thread.sleep(100);
//            for(int i = 0; i < clients.size(); ++i) {
//                Client sender = clients.get(i);
//                while (!sender.getSocket().isClosed() && sender.getIn().ready() ) {
//                    String received = sender.receiveMessage();
//                    if (received.equals("Exit")) {
//                        sender.close();
//                        clients.remove(sender);
//                        System.out.println("User \"" + sender.getSender() + "\" left chat.");
//                        continue;
//                    }
//                    messageRepository.save(new Message(sender.getSender(), received, sender.getRoomId()));
//                    System.out.println("Message \"" + received  + "\" from \""
//                            + sender.getSender() + " in room \"" + sender.getRoomId() +"\"");
//                    for(int j = 0; j < clients.size(); ++j) {
//                        Client receiver = clients.get(j);
//                        if (Objects.equals(receiver.getRoomId(), sender.getRoomId())) {
//                            receiver.sendMessage(sender.getSender() + ": " + received);
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
