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
    Socket clientSocket;
    ObjectMapper objectMapper;

    public ConnectionHandler(int port, List<Client> clients)
            throws IOException {
        this.clients = clients;
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

            clients.add(new Client(clientSocket, objectMapper));
            System.out.println(clientSocket + " connected");
            System.out.println("Clients size CH " + clients.size());
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

    public Socket getClientSocket() {
        return clientSocket;
    }
}
