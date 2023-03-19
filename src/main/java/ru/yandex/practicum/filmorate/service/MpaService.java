package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public List<Mpa> getRatings() {
        return mpaDbStorage.getRatings();
    }

    public Mpa getRatingById(int ratingId) {
        return mpaDbStorage.getRatingById(ratingId);
    }
}