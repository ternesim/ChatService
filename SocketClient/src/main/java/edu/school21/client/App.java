//package edu.school21.client;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ConnectException;
//import java.net.Socket;
//import java.util.Scanner;
//
//
//public class App {
//    public static void main(String[] args) throws IOException {
//        int port;
//        try {
//            if (args.length != 1 || !args[0].startsWith("--server-port=")) throw new IllegalArgumentException();
//            port = Integer.parseInt(args[0].replace("--server-port=", ""));
//        } catch (Exception e) {
//            System.out.println("Port parsing error");
//            return;
//        }
//
//        Socket clientSocket;
//        try {
//            clientSocket = new Socket("127.0.0.1", port);
//        } catch (ConnectException e) {
//            System.out.println("Server is not up");
//            return;
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//        String greet = in.readLine();
//        String hello = objectMapper.readValue(greet, Message.class).getText();
//        System.out.println(hello);
//
//        Listener listener = new Listener(in, clientSocket);
//        listener.start();
//
//        Scanner scanner = new Scanner(System.in);
//
//        try {
//            String input;
//            while (!clientSocket.isClosed()) {
//                input = scanner.nextLine();
//
//                Message message = new Message(listener.getLogin(), input, listener.getRoomId());
//                String json = objectMapper.writeValueAsString(message);
//                out.println(json);
//                if (input.equals("Exit")) {
//                    System.out.println("You have left the chat.");
//                    break;
//                }
//            }
//        } catch (Exception ignored) {
//        } finally {
//            scanner.close();
//            listener.interrupt();
//            clientSocket.close();
//            in.close();
//            out.close();
//        }
//    }
//
//}