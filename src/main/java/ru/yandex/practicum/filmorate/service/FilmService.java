package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void putLike(Long id, Long userId) {
        Film film = filmStorage.get(id);
        film.getLikes().add(userId);
        film.setLikesCount(film.getLikes().size());
        log.debug("Текущее количество лайков фильма: {} — {}", film.getName(), film.getLikes().size());
    }

    public void removeLike(Long id, Long userId) {
        Film film = filmStorage.get(id);
        if (!film.getLikes().contains(userId)) {
            throw new UserNotFoundException("Пользователь c таким id не найден.");
        }
        film.getLikes().remove(userId);
        film.setLikesCount(film.getLikes().size());
        log.debug("Текущее количество лайков фильма: {} — {}", film.getName(), film.getLikes().size());
    }

    public List<Film> getTop(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingLong(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
