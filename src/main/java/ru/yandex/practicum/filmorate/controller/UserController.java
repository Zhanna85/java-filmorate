package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService = new UserService();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("{} added to the service ", user);
        userService.addUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("{} added to the service ", user);
        userService.updateUser(user);
        return user;
    }

    @GetMapping
    public List<User> getListUsers() {
        return userService.getAllUsers();
    }
}