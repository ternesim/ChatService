package edu.school21.sockets.app;


import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Rest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main
{
    public static void main( String[] args ) throws IOException, InterruptedException {

        int port;
        try {
            if (args.length != 1 || !args[0].startsWith("--port=")) throw new IllegalArgumentException();
            port = Integer.parseInt(args[0].replace("--port=", ""));
        } catch (Exception e) {
            System.out.println("Port parsing error");
            return;
        }

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        Rest server = applicationContext.getBean(Rest.class);
        server.start(6667);
    }
}