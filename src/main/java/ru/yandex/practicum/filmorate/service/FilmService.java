package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.COMPARATOR;
import static ru.yandex.practicum.filmorate.Constants.DATE;
import static ru.yandex.practicum.filmorate.message.Message.*;

@Slf4j
@Service
public class FilmService extends AbstractService<Film> {

    private final UserService userService;

    public FilmService(Storage<Film> storage, UserService userService) {
        this.userService = userService;
        this.storage = storage;
    }

    @Override
    protected void dataValidator(Film film) {
        if (film.getReleaseDate().isBefore(DATE)) {
            log.error(RELEASE_DATE.getMessage() + DATE);
            throw new ValidationException(RELEASE_DATE.getMessage() + DATE);
        }
    }

    public void putLike(long id, long userId) {
        Film film = storage.find(id);
        userService.containsUser(userId);
        film.addLike(userId);
    }

    public void deleteLike(long id, long userId) {
        Film film = storage.find(id);
        userService.containsUser(userId);
        if (!film.removeLike(userId)) {
            log.error(MODEL_NOT_FOUND.getMessage() + userId);
            throw new NotFoundException(MODEL_NOT_FOUND.getMessage() + userId);
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        return (storage.getAll()
                .stream().sorted(COMPARATOR))
                .limit(count)
                .collect(Collectors.toList());
    }
}