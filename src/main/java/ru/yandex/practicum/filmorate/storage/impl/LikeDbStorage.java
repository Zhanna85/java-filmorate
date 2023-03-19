package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private JdbcTemplate jdbcTemplate;
    private FilmMapper filmMapper;
    private final GenreDbStorage genreDbStorage;

    @Override
    public void putLike(long id, long userId) {
        final String sql = "INSERT INTO popular_films (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, userId);
        final String sqlAddLike = "UPDATE films SET count_likes = count_likes + 1 WHERE film_id=?";
        jdbcTemplate.update(sqlAddLike, id);
    }

    @Override
    public void deleteLike(long id, long userId) {
        final String sql = "DELETE FROM popular_films WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, id, userId);
        final String sqlDeleteLike = "UPDATE films SET count_likes = count_likes - 1 WHERE film_id=?";
        jdbcTemplate.update(sqlDeleteLike, id);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        Map<Long, List<Genre>> listGenresByFilms = genreDbStorage.getGenreByFilm();
        String sql = "SELECT f.*, mpa.name_rating\n" +
                "FROM films AS f, ratings AS mpa\n" +
                "WHERE f.rating_id = mpa.rating_id\n" +
                "ORDER BY f.count_likes DESC, film_id ASC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> filmMapper.mapRow(rs, listGenresByFilms)), count);
    }
}