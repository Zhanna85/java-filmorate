package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.util.List;

@Component
@AllArgsConstructor
public class LikeDbStorage {

    private JdbcTemplate jdbcTemplate;
    private FilmMapper filmMapper;

    public void putLike(long id, long userId) {
        final String sql = "INSERT INTO popular_films (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, userId);
        final String sqlAddLike = "UPDATE films SET count_likes = count_likes + 1 WHERE film_id=?";
        jdbcTemplate.update(sqlAddLike, id);
    }

    public void deleteLike(long id, long userId) {
        final String sql = "DELETE FROM popular_films WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, id, userId);
        final String sqlDeleteLike = "UPDATE films SET count_likes = count_likes - 1 WHERE film_id=?";
        jdbcTemplate.update(sqlDeleteLike, id);
    }

    public List<Film> getPopularFilms(Integer count) {
        String sql = "SELECT * FROM films ORDER BY count_likes DESC, film_id ASC LIMIT ?";
        return jdbcTemplate.query(sql, filmMapper, count);
    }
}