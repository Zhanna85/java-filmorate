package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.message.Message.ADD_MODEL;
import static ru.yandex.practicum.filmorate.message.Message.UPDATED_MODEL;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserStorage storage;
    private final UserService userService;

    @Autowired
    public UserController( UserStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info(ADD_MODEL.getMessage(), user);
        storage.add(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info(UPDATED_MODEL.getMessage(), user);
        storage.update(user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId){
        userService.putFriend(id, friendId);
    }

    @GetMapping
    public List<User> getListUsers() {
        return storage.getAll();
    }

    @GetMapping("/{id}/friends")
    public List<User> getListFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return  storage.find(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getListMutualFriends(id, otherId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriend(id, friendId);
    }
}