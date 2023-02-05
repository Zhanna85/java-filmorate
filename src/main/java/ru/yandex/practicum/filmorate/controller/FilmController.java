package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.Service;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.message.Message.ADD_MODEL;
import static ru.yandex.practicum.filmorate.message.Message.UPDATED_MODEL;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Service<Film> filmService = new FilmService();

    @PostMapping
    public Film saveFilm(@Valid @RequestBody Film film) {
        log.info(ADD_MODEL.getMessage(), film);
        filmService.add(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info(UPDATED_MODEL.getMessage(), film);
        filmService.update(film);
        return film;
    }

    @GetMapping
    public List<Film> listFilms() {
        return filmService.getAll();
    }
}