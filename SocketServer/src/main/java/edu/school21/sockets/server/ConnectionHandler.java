package edu.school21.sockets.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.school21.sockets.repositories.RoomsRepository;
import edu.school21.sockets.services.UsersService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

public class ConnectionHandler extends Thread {
    List<Client> clients;
    ServerSocket serverSocket;
    UsersService usersService;
    RoomsRepository roomsRepository;
    Socket clientSocket;
    ObjectMapper objectMapper;

    public ConnectionHandler(int port, List<Client> clients, UsersService usersService, RoomsRepository roomsRepository)
            throws IOException {
        this.clients = clients;
        this.usersService = usersService;
        this.roomsRepository = roomsRepository;
        objectMapper = new ObjectMapper();
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {

            try {
                clientSocket = serverSocket.accept();
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                new AuthHandler(this).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(clientSocket + " connected");
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Client> getClients() {
        return clients;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public UsersService getUsersService() {
        return usersService;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
