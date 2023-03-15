package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import static ru.yandex.practicum.filmorate.Constants.DATE;
import static ru.yandex.practicum.filmorate.message.Message.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService extends AbstractService<Film> {

    private final Storage<Film> filmStorage;
    private final LikeStorage likeDbStorage;
    private final UserService userService;

    protected void dataValidator(Film film) {
        if (film.getReleaseDate().isBefore(DATE)) {
            log.error(RELEASE_DATE.getMessage() + DATE);
            throw new ValidationException(RELEASE_DATE.getMessage() + DATE);
        }
    }

    @Override
    public Film addModel(Film data) {
        super.addModel(data);
        return filmStorage.add(data);
    }

    @Override
    public Film updateModel(Film data) {
        super.updateModel(data);
        return filmStorage.update(data);
    }

    @Override
    public void deleteModelById(long id) {
        filmStorage.delete(id);
    }

    @Override
    public Film findModelById(long id) {
        return filmStorage.find(id);
    }

    @Override
    public List<Film> getAllModels() {
        return filmStorage.getAll();
    }

    public void putLike(long id, long userId) {
        filmStorage.find(id); // Проверяем есть ли такой фильм.
        userService.findModelById(userId); // Проверяем есть ли такой Id таблице users, иначе будет исключение.
        likeDbStorage.putLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        filmStorage.find(id); // Проверяем есть ли такой фильм.
        userService.findModelById(userId); // Проверяем есть ли такой Id таблице users, иначе будет исключение.
        likeDbStorage.deleteLike(id, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return likeDbStorage.getPopularFilms(count);
    }
}