package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Comparator;

public class Constants {
    public static final LocalDate DATE = LocalDate.of(1895, 12, 28);
    public static final Comparator<Film> COMPARATOR = Comparator.comparing(Film::getSizeListLikes).reversed();
}