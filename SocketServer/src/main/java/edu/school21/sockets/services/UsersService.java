package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.server.expetions.AuthException;

public interface UsersService {
    User signUp(String login, String password) throws AuthException;

    User signIn(String login, String password) throws AuthException;
}
