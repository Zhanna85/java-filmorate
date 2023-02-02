package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserService {
    private final Map<Integer, User> users= new HashMap<>();

    private int generateID = 0;

    private void dataValidatorUser(User user) {
        String email = user.getEmail();
        if (email.isBlank() || !email.contains("@")) {
            log.error("Email cannot be empty and must contain the \"@\" character");
            throw new ValidationException("Значение поля Email не может быть пустым " +
                    "и должен содержать символ @");
        }
        if (user.getLogin().isBlank()){
            log.error("Login may not be empty");
            throw new ValidationException("Логин не должен быть пустым или содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthday can't be in the future");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private void updateName(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public List<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }

    public User addUser(User user) {
        dataValidatorUser(user);
        if (users.containsValue(user)) {
            log.error("model User already exists");
            throw new ValidationException("Пользователь уже существует.");
        }
        generateID++;
        user.setId(generateID);
        updateName(user);
        users.put(user.getId(), user);
        log.info("{} added to the service ", user);
        return user;
    }

    public User updateUser(User user) {
        dataValidatorUser(user);
        if (!users.containsKey(user.getId())) {
            log.error("model Film by iD - " + user.getId() + " not found");
            throw new ValidationException("Пользователя c ИД " + user.getId() + " не найден.");
        }
        updateName(user);
        users.put(user.getId(), user);
        log.info("{} added to the service ", user);
        return user;
    }
}