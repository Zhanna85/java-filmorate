package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
public class FilmMapper {

    public Film mapRow(ResultSet rs, Map<Long, List<Genre>> listGenresByFilms) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("rating_id"));
        mpa.setName(rs.getString("name_rating"));
        film.setMpa(mpa);
        List<Genre> genres = listGenresByFilms.getOrDefault(rs.getLong("film_id"), new ArrayList<>());
        film.setGenres(new HashSet<>(genres));
        film.setRate(rs.getInt("count_likes"));
        return film;
    }
}