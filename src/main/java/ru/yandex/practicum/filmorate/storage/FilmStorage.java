package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage extends Storage<Film> {
    void putLike(long id, long userId);
    void deleteLike(long id, long userId);
    List<Genre> getGenres();
    Genre getGenreById(int genreId);
    List<Mpa> getRatings();
    Mpa getRatingById(int ratingId);
}