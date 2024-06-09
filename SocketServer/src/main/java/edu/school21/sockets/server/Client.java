package edu.school21.sockets.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.school21.sockets.models.User;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private  PrintWriter out;
    private  BufferedReader in;
    private  String sender;
    private  Socket socket;
    private  Long roomId;
    private User user;
    private final ObjectMapper objectMapper;

    public Client(Socket socket, PrintWriter out, BufferedReader in,
                  String sender, Long roomId, ObjectMapper objectMapper) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.sender = sender;
        this.roomId = roomId;
        this.objectMapper = objectMapper;
    }

    public Client(Socket socket, ObjectMapper objectMapper)  {
        this.socket = socket;
        this.objectMapper = objectMapper;
    }


    public void send(JSONObject json) {
        out.println(json);
    }

    JSONObject receiveRequest() {
        try {
            return new JSONObject(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("message receiving error");
        }
    }

    public void close() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Client client = (Client) object;
        return Objects.equals(out, client.out)
                && Objects.equals(in, client.in)
                && Objects.equals(sender, client.sender)
                && Objects.equals(socket, client.socket)
                && Objects.equals(roomId, client.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(out, in, sender, socket, roomId);
    }

    public Long getRoomId() {
        return roomId;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public String getSender() {
        return sender;
    }

    public Socket getSocket() {
        return socket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAuthenticated() {
        return user != null;
    }
}
