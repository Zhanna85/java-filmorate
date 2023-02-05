package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Service <T> {

    protected final Map<Integer, T> list= new HashMap<>();

    protected int generateID = 0;

    public List<T> getAll(){
        return new ArrayList<>(list.values());
    }

    public abstract T add(T type);

    public abstract T update(T type);
}
