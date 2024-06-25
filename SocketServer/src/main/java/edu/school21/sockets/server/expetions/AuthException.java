package edu.school21.sockets.server.expetions;

public class AuthException extends RuntimeException {
    public AuthException() {}
    public AuthException(String message) {
        super(message);
    }
}
