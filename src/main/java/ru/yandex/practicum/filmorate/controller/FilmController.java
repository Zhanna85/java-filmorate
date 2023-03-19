package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.message.Message.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film saveFilm(@Valid @RequestBody Film film) {
        log.info(ADD_MODEL.getMessage(), film);
        return filmService.addModel(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info(UPDATED_MODEL.getMessage(), film);
        return filmService.updateModel(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info(REQUEST_TO_LIKE.getMessage(), id, userId);
        filmService.putLike(id, userId);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info(REQUEST_BY_ID.getMessage(), id);
        return filmService.findModelById(id);
    }

    @GetMapping
    public List<Film> listFilms() {
        log.info(REQUEST_ALL.getMessage());
        return filmService.getAllModels();
    }

    @GetMapping({"/popular"})
    public List<Film> getListPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info(REQUEST_POPULAR.getMessage());
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info(DELETE_LIKE.getMessage(), id, userId);
        filmService.deleteLike(id, userId);
    }
}