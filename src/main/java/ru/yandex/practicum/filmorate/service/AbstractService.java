package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.AbstractModel;

import java.util.List;

public abstract class AbstractService<T extends AbstractModel> {

    protected abstract void dataValidator(T data);

    public T addModel(T data) {
        dataValidator(data);
        return data;
    }

    public T updateModel(T data) {
        dataValidator(data);
        return data;
    }

    public abstract void deleteModelById(long id);

    public abstract T findModelById(long id);

    public abstract List<T> getAllModels();
}