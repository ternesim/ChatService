package edu.school21.sockets.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.school21.sockets.models.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Objects;

public class Client {
    private final PrintWriter out;
    private final BufferedReader in;
    private final String sender;
    private final Socket socket;
    private final Long roomId;
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

    public void sendMessage(String text) {
        String json;
        try {
            Message message = new Message(
                    "server", text, 0L, new Timestamp(System.currentTimeMillis()));
            json = objectMapper.writeValueAsString(message);
            out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String receiveMessage() {
        try {
            String json = in.readLine();
            Message message = objectMapper.readValue(json, Message.class);
            return message.getText();
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

}
