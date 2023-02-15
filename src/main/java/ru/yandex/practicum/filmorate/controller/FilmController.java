package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.message.Message.ADD_MODEL;
import static ru.yandex.practicum.filmorate.message.Message.UPDATED_MODEL;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmStorage storage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage storage, FilmService filmService) {
        this.storage = storage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film saveFilm(@Valid @RequestBody Film film) {
        log.info(ADD_MODEL.getMessage(), film);
        storage.add(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info(UPDATED_MODEL.getMessage(), film);
        storage.update(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.putLike(id, userId);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return storage.find(id);
    }

    @GetMapping
    public List<Film> listFilms() {
        return storage.getAll();
    }

    @GetMapping({"/popular?count={count}", "/popular"})
    public List<Film> getListPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }
}