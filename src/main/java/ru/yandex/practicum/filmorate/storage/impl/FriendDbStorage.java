package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    public List<User> getListMutualFriends(long id, long otherId) {
        String sql = "SELECT u.* \n" +
                "FROM friends AS f\n" +
                "INNER JOIN users AS u ON f.friend_id = u.user_id \n" +
                "WHERE f.user_id = ?\n" +
                "AND f.friend_id in (SELECT fr.friend_id\n" +
                "                  FROM friends AS fr\n" +
                "                  WHERE fr.user_id = ?)";
        return jdbcTemplate.query(sql, new UserMapper(), id, otherId);
    }

    @Override
    public void putFriend(long id, long friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public List<User> getFriends(long id){
        String sql = "SELECT * FROM users WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(sql, new UserMapper(), id);
    }
}