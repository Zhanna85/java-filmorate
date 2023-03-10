package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Film extends AbstractModel {

    @NotBlank
    private String name; // название
    @Size(max = 200)
    private String description; // описание
    @NotNull
    private LocalDate releaseDate; // дата релиза
    @Positive
    private int duration; // продолжительность фильма
    private Mpa mpa; // id рейтинга
    private Set<Genre> genres; // жанры
}