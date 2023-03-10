package ru.yandex.practicum.filmorate.dao.mapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

public class FilmMapper implements RowMapper<Film> {
    private FilmDbStorage filmDbStorage;
    private JdbcTemplate jdbcTemplate;

    private List<Genre> getGenreByIdFilm(long id) {
        String sql = "SELECT * FROM genre WHERE genre_id IN (SELECT genre_id FROM film_genre WHERE film_id=?)";
        return jdbcTemplate.query(sql, new GenreMapper(), id);
    }
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        Mpa mpa = filmDbStorage.getRatingById(rs.getInt("rating_id"));
        film.setMpa(mpa);
        film.setGenres(new HashSet<>(getGenreByIdFilm(rs.getLong("film_id"))));
        return film;
    }
}
