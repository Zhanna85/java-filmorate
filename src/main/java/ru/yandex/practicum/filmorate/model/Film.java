package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
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
    private long duration; // продолжительность фильма
    @JsonIgnore
    private Set<Long> likes = new HashSet<>(); // лайки
    @Positive
    private int id_rating; // id рейтинга
    private Set<Integer> genres = new HashSet<>(); // жанры

    public void addLike(Long id) {
        likes.add(id);
    }

    public boolean removeLike(Long id) {
        return likes.remove(id);
    }

    public int getSizeListLikes() {
        return likes.size();
    }
}