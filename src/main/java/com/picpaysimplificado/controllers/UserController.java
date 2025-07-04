package com.picpaysimplificado.controllers;

import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.entity.User;
import com.picpaysimplificado.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/users")
public class UserController {

    private UserService userService;

public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(UserDTO userDto) throws Exception {
            User user = userService.createUser(userDto);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
