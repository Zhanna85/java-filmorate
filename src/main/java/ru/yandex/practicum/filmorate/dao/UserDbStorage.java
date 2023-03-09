package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.UserMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.message.Message.*;

@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public User add(User data) { //пересмотреть метод, поискать как вернуть ид?
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        int count = jdbcTemplate.update(sql, data.getEmail(), data.getLogin(), data.getName(), data.getBirthday());
        if (count == 0 ) {
            log.error(DUPLICATE.getMessage());
            throw new ValidationException(DUPLICATE.getMessage());
        }
        log.info(ADD_MODEL.getMessage(), data);
        return find(data.getId());
    }

    @Override
    public User update(User data) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int count = jdbcTemplate.update(sql, data.getEmail(), data.getLogin(), data.getName()
                , data.getBirthday(), data.getId());
        if (count == 0) {
            log.error(MODEL_NOT_FOUND.getMessage() + data.getId());
            throw new NotFoundException(MODEL_NOT_FOUND.getMessage() + data.getId());
        }
        return find(data.getId());
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User find(long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.query(sql, new UserMapper(), id)
                .stream().
                findAny().
                orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + id));
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    public void putFriend(long id, long friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    public List<User> getFriends(long id){
        String sql = "SELECT * FROM users WHERE user_id = (SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(sql, new UserMapper(), id);
    }
}