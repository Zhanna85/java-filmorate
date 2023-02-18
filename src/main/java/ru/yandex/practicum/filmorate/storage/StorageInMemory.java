package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.message.Message.*;

@Slf4j
public abstract class StorageInMemory<T extends AbstractModel> implements Storage<T> {

    private final Map<Long, T> list= new HashMap<>();
    private long generateID = 0L;

    protected void validationContain(long id) {
        if(!list.containsKey(id)) {
            log.error(MODEL_NOT_FOUND.getMessage() + id);
            throw new NotFoundException(MODEL_NOT_FOUND.getMessage() + id);
        }
    }

    @Override
    public T add(T data) {
        if(list.containsValue(data)) {
            log.error(DUPLICATE.getMessage());
            throw new ValidationException(DUPLICATE.getMessage());
        }
        generateID++;
        data.setId(generateID);
        list.put(data.getId(), data);
        log.info(ADD_MODEL.getMessage(), data);
        return data;
    }

    @Override
    public T update(T data) {
        validationContain(data.getId());
        list.put(data.getId(), data);
        log.info(UPDATED_MODEL.getMessage(), data);
        return data;
    }

    @Override
    public void delete(long id) {
        validationContain(id);
        list.remove(id);
    }

    @Override
    public T find(long id) {
        validationContain(id);
        return list.get(id);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(list.values());
    }
}