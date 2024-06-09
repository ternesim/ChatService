package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import edu.school21.sockets.server.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsersServiceImpl implements UsersService {

    UsersRepository usersRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(
            @Qualifier("usersRepositoryJdbcTemplateImpl") UsersRepository usersRepository,
            @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User signUp(String login, String password) throws AuthException {
        usersRepository.findByLogin(login)
                .ifPresent(x -> {throw new AuthException();});
        User user = new User(login, password);
        usersRepository.save(user);
        return user;
    }

    @Override
    public User signIn(String login, String password) throws AuthException {
        return usersRepository.findByLogin(login)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(AuthException::new);
    }

}
