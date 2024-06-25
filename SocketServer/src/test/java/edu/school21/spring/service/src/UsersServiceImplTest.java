package edu.school21.spring.service.src;

import edu.school21.sockets.server.Rest;
import edu.school21.spring.service.config.TestApplicationConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UsersServiceImplTest {

    enum RequestType {
        WELCOME, SIGN_IN, SIGN_UP, ROOMS_LIST, UNDEFINED
    }

    static final int SERVER_PORT = 6667;

    @BeforeAll
    static void startServer() {
        Thread th = new Thread(() -> {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestApplicationConfig.class);
            Rest server = applicationContext.getBean(Rest.class);
            try {
                server.start(SERVER_PORT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }});
        th.start();
    }

    @Test
    void WhenSgnInJsonRe() throws IOException, InterruptedException {
//        Thread th = new Thread(() -> {
//            try {
//                new Rest().start(6667);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        th.start();

        System.out.println("Test connecting");

        Thread.sleep(1000);


        Socket socket = new Socket("localhost", SERVER_PORT);

        System.out.println("Test Client connected");

        //Reader b = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        JSONObject json = new JSONObject();
        json.put("requestType", RequestType.ROOMS_LIST.name());
        out.println(json);

        JSONObject ans = new JSONObject(in.readLine());

        System.out.println(ans);

        JSONArray a = ans.getJSONArray("List");

        System.out.println(a.get(0));



        //out.println("Hello test");
        //System.out.println(a);
        //System.out.println(in.readLine());
        //System.out.println(in.readLine());

    }
}
