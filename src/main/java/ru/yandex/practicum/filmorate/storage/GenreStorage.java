package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getGenres();
    Genre getGenreById(int genreId);
    List<Genre> getGenreByIdFilm(long id);
}
