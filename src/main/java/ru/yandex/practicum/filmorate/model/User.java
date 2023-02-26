package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"id"}, callSuper = false)
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User extends AbstractModel{

    private long  id; // целочисленный идентификатор
    @Email
    private String email; // электронная почта
    @NotBlank
    private String login; // логин пользователя
    private String name; // имя для отображения
    @PastOrPresent
    private LocalDate birthday; // дата рождения
    @JsonIgnore
    private final Set<Long> listFriends = new HashSet<>(); // список друзей

    public void addFriend(Long id) {
        listFriends.add(id);
    }

    public void removeFriend(Long id) {
        listFriends.remove(id);
    }
}