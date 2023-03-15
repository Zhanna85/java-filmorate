package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.message.Message.EMAIL_CANNOT_BE_EMPTY;
import static ru.yandex.practicum.filmorate.message.Message.LOGIN_MAY_NOT_CONTAIN_SPACES;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<User> {

    private final Storage<User> userStorage;
    private final FriendStorage friendStorage;

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

    @Override
    public User addModel(User data) {
        super.addModel(data);
        return userStorage.add(data);
    }

    @Override
    public User updateModel(User data) {
        super.updateModel(data);
        return userStorage.update(data);
    }

    @Override
    public void deleteModelById(long id) {
        userStorage.delete(id);
    }

    @Override
    public User findModelById(long id) {
        return userStorage.find(id);
    }

    @Override
    public List<User> getAllModels() {
        return userStorage.getAll();
    }

    public void putFriend(long id, long friendId) {
        userStorage.find(id);
        userStorage.find(friendId);
        friendStorage.putFriend(id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        friendStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(long id) {
        return friendStorage.getFriends(id);
    }

    public List<User> getListMutualFriends(long id, long otherId) {
        /*Set<User> usersFriends = new HashSet<>(userStorage.getFriends(id));
        Set<User> otherFriends = new HashSet<>(userStorage.getFriends(otherId));
        return usersFriends.stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toList());*/
        return  friendStorage.getListMutualFriends(id, otherId);
    }
}