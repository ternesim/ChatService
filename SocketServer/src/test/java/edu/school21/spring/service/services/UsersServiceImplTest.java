package edu.school21.spring.service.services;

import edu.school21.sockets.server.Rest;
import edu.school21.spring.service.config.TestApplicationConfig;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UsersServiceImplTest {

    @Test
    void WhenSgnInJsonRe() throws IOException {
        Rest server = new Rest();
        server.start(667);

        Socket socket = new Socket("192.168.0.1", 667);
        //Reader b = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Writer in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        JSONObject json = new JSONObject();
        json.put("WelcomeRequest", "Hello from client");
        in.readLine(json.toString());

        //        in = new BufferedReader(new InputStreamReader(connectionHandler.getClientSocket().getInputStream()));

    }
}
