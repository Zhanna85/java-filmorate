package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static ru.yandex.practicum.filmorate.message.Message.*;

@Slf4j
public class UserService extends Service<User>{

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
        if (!list.containsKey(user.getId())) {
            log.error(MODEL_NOT_FOUND.getMessage() + user.getId());
            throw new ValidationException(MODEL_NOT_FOUND.getMessage() + user.getId());
        }
        updateName(user);
        list.put(user.getId(), user);
        log.info(UPDATED_MODEL.getMessage(), user);
        return user;
    }
}