package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.message.Message.MODEL_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    private void updateGenreByFilm(Film data) {
        final long filmId = data.getId();
        final String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
        final List<Genre> genres = data.getGenres();
        if (genres == null || genres.isEmpty()) {
            return;
        }
        final List<Genre> genresDistinct= genres.stream()
                .distinct()
                .collect(Collectors.toList());
        for (Genre genre : genresDistinct) {
            final String sqlInsert = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlInsert, filmId, genre.getId());
        }
    }

    @Override
    public Film add(Film data) {
        final String sql = "INSERT INTO films (name, description, release_date, duration, rating_id, count_likes)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement psst = connection.prepareStatement(sql,  new String[] { "film_id" });
            psst.setString(1, data.getName());
            psst.setString(2, data.getDescription());
            psst.setDate(3, Date.valueOf(data.getReleaseDate()));
            psst.setInt(4, data.getDuration());
            psst.setInt(5, data.getMpa().getId());
            psst.setInt(6, data.getRate());
            return psst;
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        updateGenreByFilm(data);
        return find(data.getId());
    }

    @Override
    public Film update(Film data) {
        final String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?," +
                "rating_id = ? WHERE film_id = ?";
        final int count = jdbcTemplate.update(sql, data.getName(), data.getDescription(), data.getReleaseDate(),
                data.getDuration(), data.getMpa().getId(), data.getId());
        updateGenreByFilm(data);
        if (count == 0) {
            log.error(MODEL_NOT_FOUND.getMessage() + data.getId());
            throw new NotFoundException(MODEL_NOT_FOUND.getMessage() + data.getId());
        }

        updateGenreByFilm(data);
        return find(data.getId());
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film find(long id) {
        final String sql = "SELECT * FROM films WHERE film_id = ?";
        return jdbcTemplate.query(sql, filmMapper, id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + id));
    }

    @Override
    public List<Film> getAll() {
        final String sql = "SELECT * FROM films ORDER BY film_id ASC";
        return jdbcTemplate.query(sql,filmMapper);
    }
}