package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.Constants.DATE;
import static ru.yandex.practicum.filmorate.message.Message.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> list= new HashMap<>();

    private long generateID = 0L;

    private void dataValidator(Film film) {
        if (film.getReleaseDate().isBefore(DATE)) {
            log.error(RELEASE_DATE.getMessage() + DATE);
            throw new ValidationException(RELEASE_DATE.getMessage() + DATE);
        }
    }

    private void validationContainFilm(long id) {
        if(!list.containsKey(id)) {
            log.error(MODEL_NOT_FOUND.getMessage() + id);
            throw new NotFoundException(MODEL_NOT_FOUND.getMessage() + id);
        }
    }

    @Override
    public Film add(Film film) {
        dataValidator(film);
        if(list.containsValue(film)) {
            log.error(DUPLICATE.getMessage());
            throw new ValidationException(DUPLICATE.getMessage());
        }
        generateID++;
        film.setId(generateID);
        list.put(generateID, film);
        log.info(ADD_MODEL.getMessage(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        validationContainFilm(film.getId());
        dataValidator(film);
        list.put(film.getId(), film);
        log.info(UPDATED_MODEL.getMessage(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(list.values());
    }

    @Override
    public void delete(long id) {
        validationContainFilm(id);
        list.remove(id);
    }

    @Override
    public Film find(long id) {
        validationContainFilm(id);
        return list.get(id);
    }
}