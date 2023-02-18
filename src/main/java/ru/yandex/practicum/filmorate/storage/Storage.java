package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.AbstractModel;

import java.util.List;

public interface Storage <T extends AbstractModel> {
    T add(T data);

    T update(T data);

    void delete(long id);

    T find(long id);

    List<T> getAll();
}