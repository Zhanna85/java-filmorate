package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Service;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.message.Message.ADD_MODEL;
import static ru.yandex.practicum.filmorate.message.Message.UPDATED_MODEL;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Service<User> userService = new UserService();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info(ADD_MODEL.getMessage(), user);
        userService.add(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info(UPDATED_MODEL.getMessage(), user);
        userService.update(user);
        return user;
    }

    @GetMapping
    public List<User> getListUsers() {
        return userService.getAll();
    }
}