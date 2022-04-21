package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final HashMap<String, Film> films = new HashMap<>();
    private int Id = 1;

    @GetMapping
    public Collection<Film> getAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public void create(@Valid @RequestBody Film film) throws ValidationException {
        validateFilm(film);
        isInBase(film);
        film.setId(Id++);
        films.put(film.getName(), film);
        log.debug("Текущее количество фильмов: {}", films.size());
    }

    @PutMapping
    public void update(@Valid @RequestBody Film film) throws ValidationException {
        validateFilm(film);
        films.put(film.getName(), film);
        log.debug("Текущее количество фильмов: {}", films.size());
    }

    public void validateFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.warn("Название не может быть пустым");
            throw new ValidationException("Ошибка валидации");
        }
        if(film.getDescription().length() > 200) {
            log.warn("Длина описания больше 200 символова");
            throw new ValidationException("Ошибка валидации");
        }
        if (checkReleaseDate(film.getReleaseDate())) {
            log.warn("Дата релиза не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Ошибка валидации");
        }
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.warn("Продолжительность не может быть отрицательной");
            throw new ValidationException("Ошибка валидации");
        }
    }

    private static boolean checkReleaseDate(LocalDate date) {
        return date.isBefore(LocalDate.of(1895, 12, 28));
    }

    public void isInBase(Film film) throws ValidationException {
        if (films.containsKey(film.getName())) {
            if (films.get(film.getName()).getReleaseDate().isEqual(film.getReleaseDate())) {
                log.warn("Такой фильм уже есть в базе");
                throw new ValidationException("Ошибка валидации");
            }
        }
    }

    public HashMap<String, Film> getFilms() {
        return films;
    }
}
