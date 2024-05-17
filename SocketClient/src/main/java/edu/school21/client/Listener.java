package edu.school21.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.BufferedReader;
import java.net.Socket;

public class Listener extends Thread {

    private final BufferedReader in;
    private final Socket socket;
    ObjectMapper objectMapper;
    Long roomId;
    String login;

    public Listener(BufferedReader in, Socket socket) {
        this.in = in;
        this.socket = socket;
        objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        roomId = null;
        login = "anonymous";
    }

    public void run() {
        try {

            while (!Thread.interrupted() || !socket.isClosed()) {

                Thread.sleep(1000);
                while (in.ready()) {
                    String input = in.readLine();
                    Message m = objectMapper.readValue(input, Message.class);

                    if (m.getText().startsWith("!!!LOGIN!!!")) {
                        login = m.getText().replace("!!!LOGIN!!!", "");
                        continue;
                    }
                    if (m.getText().startsWith("!!!roomId!!!")) {
                        roomId = Long.valueOf(m.getText().replace("!!!roomId!!!", ""));
                        continue;
                    }

                    System.out.println(m.getText());

                    if (m.getText().equals("Server has been stopped")
                        || m.getText().equals("You have left the chat.")) {
                        socket.close();
                        System.out.println("Disconnected");
                        break;
                    }


                }
            }

            if (socket.isClosed()) {
                System.out.println("Connection closed");
            }

        } catch (InterruptedException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getLogin() {
        return login;
    }
}