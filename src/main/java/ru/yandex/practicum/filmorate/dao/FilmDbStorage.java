package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.dao.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.dao.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.message.Message.MODEL_NOT_FOUND;

@Slf4j
@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    private void updateGenreByFilm(Film data) {
        final long film_id = data.getId();
        final String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, film_id);
        final List<Genre> genres = new ArrayList<>(data.getGenres());
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                final String sqlInsert = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
                jdbcTemplate.update(sqlInsert, film_id, genre.getGenre_id());
            }
        }
    }

    @Override
    public Film add(Film data) {
        String sql = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement psst = connection.prepareStatement(sql,  new String[] { "film_id" });
            psst.setString(1, data.getName());
            psst.setString(2, data.getDescription());
            psst.setDate(3, Date.valueOf(data.getReleaseDate()));
            psst.setInt(4, data.getDuration());
            psst.setInt(5, data.getMpa().getId());
            return psst;
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        updateGenreByFilm(data);
        return data;
    }

    @Override
    public Film update(Film data) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?," +
                "rating_id = ? WHERE film_id = ?";
        int count = jdbcTemplate.update(sql, data.getName(), data.getDescription(), data.getReleaseDate(),
                data.getDuration(), data.getMpa().getId(), data.getId());
        if (count == 0) {
            log.error(MODEL_NOT_FOUND.getMessage() + data.getId());
            throw new NotFoundException(MODEL_NOT_FOUND.getMessage() + data.getId());
        }

        updateGenreByFilm(data);
        return find(data.getId());
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql, new FilmMapper(), id);
    }

    @Override
    public Film find(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        return jdbcTemplate.query(sql, new FilmMapper(), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + id));
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    public void putLike(long id, long userId) {
        String sql = "INSERT INTO popular_films (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, userId);
    }

    public void deleteLike(long id, long userId) {
        String sql = "DELETE FROM popular_films WHERE film_id = ? AND user_id = ?)";
        jdbcTemplate.update(sql, id, userId);
    }

    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    public Genre getGenreById(int genreId) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        return jdbcTemplate.query(sql, new GenreMapper(), genreId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + genreId));

    }

    public List<Mpa> getRatings() {
        String sql = "SELECT rating_id, name_rating FROM ratings";
        return jdbcTemplate.query(sql, new MpaMapper());
    }

    public Mpa getRatingById(int ratingId) {
        String sql = "SELECT rating_id, name_rating FROM ratings WHERE rating_id = ?";
        return jdbcTemplate.query(sql, new MpaMapper(), ratingId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + ratingId));
    }
}