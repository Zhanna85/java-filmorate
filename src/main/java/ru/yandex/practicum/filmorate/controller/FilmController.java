package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService = new FilmService();

    @PostMapping
    public Film saveFilm(@Valid @RequestBody Film film) {
        log.info("{} added to the service ", film);
        filmService.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("{} updated to the service ", film);
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping
    public List<Film> listFilms() {
        return filmService.getAllFilms();
    }
}