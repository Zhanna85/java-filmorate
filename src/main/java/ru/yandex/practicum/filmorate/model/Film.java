package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id; // целочисленный идентификатор
    @NotBlank
    private String name; // название
    @Size(max = 200)
    private String description; // описание
    @NotNull
    private LocalDate releaseDate; // дата релиза
    @Positive
    private long duration; // продолжительность фильма
}