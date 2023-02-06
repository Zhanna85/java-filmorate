package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void before(){
        filmController = new FilmController();
    }

    @Test
    void saveFilm() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1999, 1, 1));
        film.setDuration(100);

        filmController.saveFilm(film);
        List<Film> filmList = filmController.listFilms();

        assertEquals(1, filmList.size(), "Размер списка фильмов не соответствует ожидаемому");
        assertEquals(1, filmList.get(0).getId(), "ID сформирован не верно");
    }

    @Test
    void shouldThrowExceptionSaveDuplicateFilm() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1999, 1, 1));
        film.setDuration(100);
        filmController.saveFilm(film);

        Film film2 = new Film();
        film2.setName("film");
        film2.setDescription("Description");
        film2.setReleaseDate(LocalDate.of(1999, 1, 1));
        film2.setDuration(100);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.saveFilm(film2));

        assertEquals("the model already exists", exception.getMessage()
                , "exception message проверки на дубликат не верна");

        List<Film> filmList = filmController.listFilms();

        assertEquals(1, filmList.size(), "Размер списка фильмов не соответствует ожидаемому");
        assertEquals(1, filmList.get(0).getId(), "ID сформирован не верно");
    }

    @Test
    void shouldThrowExceptionCreatingInvalidDateFilm() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1888, 1, 1));
        film.setDuration(100);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.saveFilm(film));

        assertEquals("The release date can't be earlier - 1895-12-28", exception.getMessage()
                , "exception message проверки дата релиза не верна");
    }

    @Test
    void add2FilmToTheMap() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1999, 1, 1));
        film.setDuration(100);
        filmController.saveFilm(film);

        Film film2 = new Film();
        film2.setName("film2");
        film2.setDescription("Description");
        film2.setReleaseDate(LocalDate.of(1998, 1, 1));
        film2.setDuration(150);
        filmController.saveFilm(film2);

        List<Film> filmList = filmController.listFilms();

        assertEquals(2, filmList.size(), "Размер списка фильмов не соответствует ожидаемому");
        assertEquals(2, filmList.get(1).getId(), "ID сформирован не верно");
        assertEquals(film, filmList.get(0), "Модели Film не соответствуют");
        assertEquals(film2, filmList.get(1), "Модели Film не соответствуют");
    }

    @Test
    void updateFilmInvalidIdShouldThrowException() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1999, 1, 1));
        film.setDuration(100);
        filmController.saveFilm(film);
        List<Film> filmList = filmController.listFilms();

        assertEquals(1, filmList.size(), "Размер списка фильмов не соответствует ожидаемому");
        assertEquals(1, filmList.get(0).getId(), "ID сформирован не верно");
        assertEquals(film, filmList.get(0), "Модели Film не соответствуют");

        Film film2 = new Film();
        film2.setId(2);
        film2.setName("film2");
        film2.setDescription("Description");
        film2.setReleaseDate(LocalDate.of(1998, 1, 1));
        film2.setDuration(150);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film2));
        assertEquals("model was not found by the passed ID: 2", exception.getMessage()
                , "exception проверки неверный");
    }

    @Test
    void updateFilm() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1999, 1, 1));
        film.setDuration(100);
        filmController.saveFilm(film);
        List<Film> filmList = filmController.listFilms();

        assertEquals(1, filmList.size(), "Размер списка фильмов не соответствует ожидаемому");
        assertEquals(1, filmList.get(0).getId(), "ID сформирован не верно");
        assertEquals(film, filmList.get(0), "Модели Film не соответствуют");

        Film film2 = new Film();
        film2.setId(1);
        film2.setName("film2");
        film2.setDescription("Description");
        film2.setReleaseDate(LocalDate.of(1998, 1, 1));
        film2.setDuration(150);
        filmController.updateFilm(film2);

        filmList = filmController.listFilms();

        assertEquals(1, filmList.size(), "Размер списка фильмов не соответствует ожидаемому");
        assertEquals(1, filmList.get(0).getId(), "ID сформирован не верно");
        assertEquals(film2, filmList.get(0), "Модели Film не соответствуют");
    }
}