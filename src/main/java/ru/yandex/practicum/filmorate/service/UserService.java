package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.message.Message.EMAIL_CANNOT_BE_EMPTY;
import static ru.yandex.practicum.filmorate.message.Message.LOGIN_MAY_NOT_CONTAIN_SPACES;

@Slf4j
@Service
public class UserService extends AbstractService<User> {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    protected void dataValidator(User data) {
        if (data.getEmail().isBlank()) {
            log.error(EMAIL_CANNOT_BE_EMPTY.getMessage());
            throw new ValidationException(EMAIL_CANNOT_BE_EMPTY.getMessage());
        }
        if (data.getLogin().contains(" ")){
            log.error(LOGIN_MAY_NOT_CONTAIN_SPACES.getMessage());
            throw new ValidationException(LOGIN_MAY_NOT_CONTAIN_SPACES.getMessage());
        }
        updateName(data);
    }

    private void updateName(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }

    /*public User addUser(User user) {
        dataValidator(user);
        updateName(user);
        return storage.add(user);
    }

    public User updateUser(User user) {
        dataValidator(user);
        updateName(user);
        return storage.update(user);
    }

    public void deleteUserById(long id) {
        storage.delete(id);
    }

    public User findUserById(long id) {
        return storage.find(id);
    }

    public List<User> getAllUsers() {
        return storage.getAll();
    }*/

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
        User user = storage.find(id);
        return user.getListFriends().stream()
                .map(storage::find)
                .collect(Collectors.toList());
    }

    public List<User> getListMutualFriends(long id, long otherId) {
        User user = storage.find(id);
        User other = storage.find(otherId);
        return user.getListFriends().stream()
                .filter(other.getListFriends()::contains)
                .map(storage::find)
                .collect(Collectors.toList());
    }
}