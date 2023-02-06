package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.message.Message.*;

@Slf4j
public class FilmService extends Service<Film>{
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);

    private void dataValidator(Film film) {
        if (film.getReleaseDate().isBefore(DATE)) {
            log.error(RELEASE_DATE.getMessage() + DATE);
            throw new ValidationException(RELEASE_DATE.getMessage() + DATE);
        }
    }

    @Override
    public Film add(Film film){
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
        if(!list.containsKey(film.getId())) {
            log.error(MODEL_NOT_FOUND.getMessage() + film.getId());
            throw new ValidationException(MODEL_NOT_FOUND.getMessage() + film.getId());
        }
        dataValidator(film);
        list.put(film.getId(), film);
        log.info(UPDATED_MODEL.getMessage(), film);
        return film;
    }
}