package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.message.Message.MODEL_NOT_FOUND;

@RequiredArgsConstructor
@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        final String sql = "SELECT * FROM genre ORDER BY genre_id ASC";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public Genre getGenreById(int genreId) {
        final String sql = "SELECT * FROM genre WHERE genre_id = ?";
        return jdbcTemplate.query(sql, new GenreMapper(), genreId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + genreId));

    }

    @Override
    public Set<Genre> getGenreByIdFilm(long id) {
        String sql = "SELECT * FROM genre WHERE genre_id IN (SELECT genre_id FROM film_genre WHERE film_id=?) " +
                "ORDER BY genre_id ASC";
        return new HashSet<>(jdbcTemplate.query(sql, new GenreMapper(), id)) ;
    }
}