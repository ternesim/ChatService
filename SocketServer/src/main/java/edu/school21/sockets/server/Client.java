package edu.school21.sockets.server;

import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private  PrintWriter out;
    private  BufferedReader in;
    private  Socket socket;
    private User user;
    private Room room;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(JSONObject json) {
        out.println(json);
    }

    JSONObject receiveRequest() throws IOException {
        String s = in.readLine();
        return new JSONObject(s);
//        try {
//            String s = in.readLine();
//            return new JSONObject(s);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("message receiving error");
//        }
    }

    public void closeConnection() {
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
                && Objects.equals(socket, client.socket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(out, in, socket);
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
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

    public boolean isInRoom() {
        return room != null;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
