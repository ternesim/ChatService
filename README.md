# Message Application

This project aims to implement a message application that allows multiple users to exchange messages. The application consists of a client and server side.

## Server Side

When the server is started, three threads are launched:

- **Connection thread**: This thread waits for incoming client connections and spawns an authorization thread for each user. This allows multiple users to authorize simultaneously without blocking each other.

- **Authorization thread**: In this thread, users is required to sign up if it is their first visit to the app or input their credentials. After successful authorization, the user can create their own chat room or join an existing one. When a user enters a room, the last 30 messages are displayed on the screen.

- **Main thread**: This thread is responsible for exchanging messages between clients in a chat room. It also checks for server command inputs. Currently, only the server stop command is supported. All messages exchanged between users are stored in the message repository.

The server-side application is implemented using the Spring framework. Configuration is done using properties files and bean dependency injection. Communication between the server and the client happens using the socket API in JSON format.

## Client Side

The client-side application consists of two threads:

- **Input thread**: This thread waits for user input and sends it to the server.

- **Output thread**: This thread waits for server messages and displays them in the terminal.