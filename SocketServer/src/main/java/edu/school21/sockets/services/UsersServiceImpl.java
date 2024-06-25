package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import edu.school21.sockets.server.expetions.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsersServiceImpl implements UsersService {

    UsersRepository usersRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(
            @Qualifier("usersRepositoryImpl") UsersRepository ur,
            @Qualifier("passwordEncoder") PasswordEncoder pe) {
        this.usersRepository = ur;
        this.passwordEncoder = pe;
    }

    @Override
    public User signUp(String login, String password) throws AuthException {
        usersRepository.findByLogin(login)
                .ifPresent(x -> {
                    throw new AuthException("User name is occupied");
                });
        User user = new User(login, password);
        usersRepository.save(user);
        return user;
    }

    @Override
    public User signIn(String login, String password) throws AuthException {
        return usersRepository.findByLogin(login)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new AuthException("User doesnt exist or password are wrong"));
    }

}
