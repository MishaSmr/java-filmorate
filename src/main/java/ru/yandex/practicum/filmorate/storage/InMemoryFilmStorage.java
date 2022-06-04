package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@Component("inMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    private long id = 1;
    private final FilmValidator validator = new FilmValidator();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film get(long id) {
        if (!films.containsKey(id)) {
            log.warn("Фильм c таким id не найден.");
            throw new FilmNotFoundException("Фильм c таким id не найден.");
        }
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        validator.validateFilm(film);
        isInBase(film);
        film.setId(id++);
        //film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.debug("Текущее количество фильмов: {}", films.size());
        return film;
    }

    @Override
    public void remove(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм не найден.");
            throw new FilmNotFoundException("Фильм не найден.");
        }
        films.remove(film.getId());
    }

    @Override
    public Film update(Film film) {
        validator.validateFilm(film);
        Film oldFilm = films.get(film.getId());
        //film.setLikes(oldFilm.getLikes());
        //film.setLikesCount(oldFilm.getLikesCount());
        films.put(film.getId(), film);
        log.debug("Текущее количество фильмов: {}", films.size());
        return film;
    }

    public void isInBase(Film film) throws ValidationException {
        for (Film f : films.values()) {
            if (f.getName().equals(film.getName()) && f.getReleaseDate().equals(film.getReleaseDate())) {
                log.warn("Такой фильм уже есть в базе");
                throw new ValidationException("Ошибка валидации");
            }
        }
    }

    public HashMap<Long, Film> getFilms() {
        return films;
    }

    public FilmValidator getValidator() {
        return validator;
    }
}
