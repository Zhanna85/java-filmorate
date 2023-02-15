package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNullElse;
import static ru.yandex.practicum.filmorate.Constants.NUMBER_POPULAR_MOVIES;
import static ru.yandex.practicum.filmorate.message.Message.MODEL_NOT_FOUND;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public void putLike(long id, long userId) {
        Film film = storage.find(id);
        film.addLike(userId);
    }

    public void deleteLike(long id, long userId) {
        Film film = storage.find(id);
        if (!film.removeLike(userId)) {
            log.error(MODEL_NOT_FOUND.getMessage() + userId);
            throw new NotFoundException(MODEL_NOT_FOUND.getMessage() + userId);
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        return (storage.getAll()
                .stream().sorted(Comparator.comparing(Film::getSizeListLikes).reversed()))
                .limit(requireNonNullElse(count, NUMBER_POPULAR_MOVIES))
                .collect(Collectors.toList());
    }
}