package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void putFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    List<User> getFriends(long id);

    List<User> getListMutualFriends(long id, long otherId);
}