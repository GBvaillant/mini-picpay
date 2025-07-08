package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.entity.User;
import com.picpaysimplificado.entity.UserType;
import com.picpaysimplificado.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateTransaction (User sender, BigDecimal amount) throws Exception {

        if(sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuário do tipo lo  jista não pode realizar transações");
        }

        if(sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    public User findUserById (Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public User createUser(UserDTO data) throws Exception {
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
