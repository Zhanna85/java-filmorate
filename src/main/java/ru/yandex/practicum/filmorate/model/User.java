package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

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
}