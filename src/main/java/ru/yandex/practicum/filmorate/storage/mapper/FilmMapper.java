package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@AllArgsConstructor
public class FilmMapper implements RowMapper<Film> {

    private GenreStorage genreDbStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
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
        film.setGenres(genreDbStorage.getGenreByIdFilm(rs.getLong("film_id")));
        film.setRate(rs.getInt("count_likes"));
        return film;
    }
}