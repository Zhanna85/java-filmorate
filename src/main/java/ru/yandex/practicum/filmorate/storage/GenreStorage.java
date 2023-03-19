package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

public interface GenreStorage {
    List<Genre> getGenres();

    Genre getGenreById(int genreId);

    Map<Long, List<Genre>> getGenreByFilm();
}