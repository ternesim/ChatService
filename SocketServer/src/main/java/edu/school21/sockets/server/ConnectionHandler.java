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
    final List<Client> clients;
    ServerSocket serverSocket;
    Socket clientSocket;

    public ConnectionHandler(int port, List<Client> clients) throws IOException {
        this.clients = clients;
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                try {
                    clientSocket = serverSocket.accept();
                    clients.add(new Client(clientSocket));
                } catch (SocketTimeoutException ignored) {}
            }
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Connection thread error");
        }
    }
}
