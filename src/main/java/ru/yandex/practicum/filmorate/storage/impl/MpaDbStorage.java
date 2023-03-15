package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.message.Message.MODEL_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Mpa> getRatings() {
        final String sql = "SELECT rating_id, name_rating FROM ratings ORDER BY rating_id ASC";
        return jdbcTemplate.query(sql, new MpaMapper());
    }

    public Mpa getRatingById(int ratingId) {
        final String sql = "SELECT * FROM ratings WHERE rating_id = ?";
        return jdbcTemplate.query(sql, new MpaMapper(), ratingId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + ratingId));
    }
}