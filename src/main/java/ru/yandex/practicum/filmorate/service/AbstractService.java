package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public abstract class AbstractService<T extends AbstractModel> {

    protected Storage<T> storage;
    protected abstract void dataValidator(T data);

    public T addModel(T data) {
        dataValidator(data);
        return storage.add(data);
    }

    public T updateModel(T data) {
        dataValidator(data);
        return storage.update(data);
    }

    public void deleteModelById(long id) {
        storage.delete(id);
    }

    public T findModelById(long id) {
        return storage.find(id);
    }

    public List<T> getAllModels() {
        return storage.getAll();
    }
}