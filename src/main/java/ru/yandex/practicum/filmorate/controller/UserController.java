package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.message.Message.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info(ADD_MODEL.getMessage(), user);
        return userService.addModel(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info(UPDATED_MODEL.getMessage(), user);
        userService.updateModel(user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId){
        log.info(ADD_FRIEND.getMessage(), id, friendId);
        userService.putFriend(id, friendId);
    }

    @GetMapping
    public List<User> getListUsers() {
        log.info(REQUEST_ALL.getMessage());
        return userService.getAllModels();
    }

    @GetMapping("/{id}/friends")
    public List<User> getListFriends(@PathVariable long id) {
        log.info(REQUEST_LIST_FRIENDS.getMessage(), id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.info(REQUEST_BY_ID.getMessage(), id);
        return  userService.findModelById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info(REQUEST_MUTUAL_FRIENDS.getMessage(), id, otherId);
        return userService.getListMutualFriends(id, otherId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info(DELETE_FRIENDS.getMessage(), friendId, id);
        userService.deleteFriend(id, friendId);
    }
}