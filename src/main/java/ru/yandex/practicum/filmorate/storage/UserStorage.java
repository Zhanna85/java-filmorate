package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends Storage<User> {
    void putFriend(long id, long friendId);
    void deleteFriend(long id, long friendId);
    List<User> getFriends(long id);
}