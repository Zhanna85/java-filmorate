package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.*;

import static ru.yandex.practicum.filmorate.message.Message.MODEL_NOT_FOUND;

@RequiredArgsConstructor
@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

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
    public Map<Long, List<Genre>> getGenreByFilm() {
        String sql = "SELECT fg.film_id, g.* FROM genre AS g, film_genre fg WHERE g.genre_id = fg.genre_id";
        Map<Long, List<Genre>> listGenresByFilm = jdbcTemplate.query(sql
                , rs -> {
                    Map<Long, List<Genre>> list = new HashMap<>();

                    while (rs.next()) {
                        long idFilm = rs.getLong("film_id");
                        List<Genre> listGenre = list.getOrDefault(idFilm, new ArrayList<>());
                        listGenre.add(genreMapper.mapRow(rs, rs.getRow()));
                        list.put(idFilm, listGenre);
                    }
                    return list;
                });
        return listGenresByFilm;
    }
}