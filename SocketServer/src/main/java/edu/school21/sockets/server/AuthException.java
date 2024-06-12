package edu.school21.sockets.server;

public class AuthException extends RuntimeException {
    public AuthException() {}
    public AuthException(String message) {
        super(message);
    }
}
