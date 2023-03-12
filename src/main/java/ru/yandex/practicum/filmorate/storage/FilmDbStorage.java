package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.message.Message.MODEL_NOT_FOUND;

@Slf4j
@Component

public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

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

    private List<Genre> getGenreByIdFilm(long id) {
        String sql = "SELECT * FROM genre WHERE genre_id IN (SELECT genre_id FROM film_genre WHERE film_id=?) " +
                "ORDER BY genre_id ASC";
        return jdbcTemplate.query(sql, new GenreMapper(), id);
    }

    private Film mapRow(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        int mpaId = rs.getInt("rating_id");
        Mpa mpa = getRatingById(mpaId);
        film.setMpa(mpa);
        film.setGenres(getGenreByIdFilm(rs.getLong("film_id")));
        film.setRate(rs.getInt("count_likes"));
        return film;
    }

    @Override
    public Mpa getRatingById(int ratingId) {
        final String sql = "SELECT * FROM ratings WHERE rating_id = ?";
        return jdbcTemplate.query(sql, new MpaMapper(), ratingId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + ratingId));
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
        return jdbcTemplate.query(sql, ((rs, rowNum) -> mapRow(rs)), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + id));
    }

    @Override
    public List<Film> getAll() {
        final String sql = "SELECT * FROM films ORDER BY film_id ASC";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> mapRow(rs)));
    }

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
    public List<Mpa> getRatings() {
        final String sql = "SELECT rating_id, name_rating FROM ratings ORDER BY rating_id ASC";
        return jdbcTemplate.query(sql, new MpaMapper());
    }
}