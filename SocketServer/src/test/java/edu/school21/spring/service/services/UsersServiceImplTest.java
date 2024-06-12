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
    void WhenSgnInJsonRe() throws IOException, InterruptedException {
        Thread th = new Thread(() -> {
            try {
                new Rest().start(6667);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        th.start();

        System.out.println("connecting");

        Thread.sleep(1000);


        Socket socket = new Socket("localhost", 6667);

        System.out.println("Client connected");

        //Reader b = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        JSONObject json = new JSONObject();
        json.put("WelcomeRequest", "Hello from client");
        out.println(json);
        //out.println("Hello test");
        System.out.println(in.readLine());
        //System.out.println(in.readLine());
        //System.out.println(in.readLine());

    }
}
