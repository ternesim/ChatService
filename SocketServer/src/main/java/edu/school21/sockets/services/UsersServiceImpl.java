package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
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
    public boolean signUp(String login, String password) {
        Optional<User> optionalUser = usersRepository.findByLogin(login);
        if (optionalUser.isPresent()) {
            return false;
        } else {
            usersRepository.save(new User(login, passwordEncoder.encode(password)));
            return true;
        }
    }

    @Override
    public boolean signIn(User user) {
        Optional<User> optionalUser = usersRepository.findByLogin(user.getLogin());
        if (optionalUser.isPresent()) {
            System.out.println(optionalUser.get());
            user.setId(optionalUser.get().getId());
            return passwordEncoder.matches(user.getPassword(), optionalUser.get().getPassword());
        } else return false;
    }

//    @Override
//    public boolean signIn1(String login, String password) {
//        Optional<User> optionalUser = usersRepository.findByLogin(login);
//        if (optionalUser.isPresent()) {
//            System.out.println(optionalUser.get());
//            return passwordEncoder.matches(password, optionalUser.get().getPassword());
//        } else return false;
//    }
}
