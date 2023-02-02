package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FilmService {

    private static final LocalDate date = LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films= new HashMap<>();

    private int generateID = 0;

    private void dataValidator(Film film) {
        if(film.getReleaseDate().isBefore(date)) {
            log.error("The release date can't be earlier - " + date);
            throw new ValidationException("Дата релиза не может быть раньше - " + date);
        }
        if(film.getName().isBlank()) {
            log.error("Name may not be empty");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Description the max length is 200 characters");
            throw new ValidationException("Максимальная длина описания должна быть - 200 символов");
        }
        if (film.getDuration() <= 0) {
            log.error("Duration of the film cannot be negative");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    public List<Film> getAllFilms(){
        return new ArrayList<>(films.values());
    }

    public Film addFilm(Film film){
        dataValidator(film);
        if(films.containsValue(film)) {
            log.error("model Film already exists");
            throw new ValidationException("Фильм уже существует");
        }
        generateID++;
        film.setId(generateID);
        films.put(generateID, film);
        log.info("{} added to the service ", film);
        return film;
    }

    public Film updateFilm(Film film) {
        if(!films.containsKey(film.getId())) {
            log.error("model Film by iD - " + film.getId() + " not found");
            throw new ValidationException("Фильм по ИД - " + film.getId() + " не найден.");
        }
        dataValidator(film);
        films.put(film.getId(), film);
        log.info("{} added to the service ", film);
        return film;
    }
}