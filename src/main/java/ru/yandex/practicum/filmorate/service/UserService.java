package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void putFriend(long id, long friendId) {
        User user = storage.find(id);
        User friend = storage.find(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
    }

    public void deleteFriend(long id, long friendId) {
        User user = storage.find(id);
        User friend = storage.find(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(id);
    }

    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>();
        User user = storage.find(id);
        for (Long idFriend : user.getListFriends()) {
            friends.add(storage.find(idFriend));
        }
        return friends;
    }

    public List<User> getListMutualFriends(long id, long otherId) {
        User user = storage.find(id);
        User other = storage.find(otherId);
        List<User> friends = new ArrayList<>();

        Set<Long> intersectFriends = user.getListFriends().stream()
                .filter(other.getListFriends()::contains)
                .collect(Collectors.toSet());

        for (Long idFriend : intersectFriends) {
            friends.add(storage.find(idFriend));
        }
        return friends;
    }
}