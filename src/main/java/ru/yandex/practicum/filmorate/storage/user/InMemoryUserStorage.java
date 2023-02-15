package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.message.Message.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> list= new HashMap<>();

    private long generateID = 0L;

    private void dataValidatorUser(User user) {
        if (user.getEmail().isBlank()) {
            log.error(EMAIL_CANNOT_BE_EMPTY.getMessage());
            throw new ValidationException(EMAIL_CANNOT_BE_EMPTY.getMessage());
        }
        if (user.getLogin().contains(" ")){
            log.error(LOGIN_MAY_NOT_CONTAIN_SPACES.getMessage());
            throw new ValidationException(LOGIN_MAY_NOT_CONTAIN_SPACES.getMessage());
        }
    }

    private void updateName(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void validationContainUser(long id) {
        if (!list.containsKey(id)) {
            log.error(MODEL_NOT_FOUND.getMessage() + id);
            throw new NotFoundException(MODEL_NOT_FOUND.getMessage() + id);
        }
    }

    @Override
    public User add(User user) {
        dataValidatorUser(user);
        if (list.containsValue(user)) {
            log.error(DUPLICATE.getMessage());
            throw new ValidationException(DUPLICATE.getMessage());
        }
        generateID++;
        user.setId(generateID);
        updateName(user);
        list.put(user.getId(), user);
        log.info(ADD_MODEL.getMessage(), user);
        return user;
    }

    @Override
    public User update(User user) {
        dataValidatorUser(user);
        validationContainUser(user.getId());
        updateName(user);
        list.put(user.getId(), user);
        log.info(UPDATED_MODEL.getMessage(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(list.values());
    }

    @Override
    public void delete(long id) {
        validationContainUser(id);
        list.remove(id);
    }

    @Override
    public User find(long id) {
        validationContainUser(id);
        return list.get(id);
    }
}