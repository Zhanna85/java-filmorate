package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {
    void putLike(long id, long userId);

    void deleteLike(long id, long userId);

    List<Film> getPopularFilms(Integer count);
}